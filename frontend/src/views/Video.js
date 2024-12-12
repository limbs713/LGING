import React, {useRef, useEffect, useState, useCallback} from 'react';
import {MediaControls} from '@enact/sandstone/MediaPlayer';
import VideoPlayer from '@enact/sandstone/VideoPlayer';
import Button from '@enact/sandstone/Button';
import Alert from '@enact/sandstone/Alert';
import axios from 'axios';
import Popup from '@enact/sandstone/Popup';
import Spinner from '@enact/sandstone/Spinner';
import {InputField} from '@enact/sandstone/Input';
import Scroller from '@enact/ui/Scroller';
import Icon from '@enact/sandstone/Icon';
import {useNavigate, useParams} from 'react-router-dom';
import styled from 'styled-components';
const serverUri = process.env.REACT_APP_SERVER_URI;

const CustomSpinner = styled(Spinner)`
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
`;

const Video = props => {
	const videoRef = useRef();
	const navigate = useNavigate();
	const [loading, setLoading] = useState(true);
	const [timestamp, setTimestamp] = useState(0);
	const [alertOpen, setAlertOpen] = useState(false);
	const [popupOpen, setPopupOpen] = useState(false);
	const [popupMessage, setPopupMessage] = useState('');
	const [commentOpen, setCommentOpen] = useState(false);
	const [comments, setComments] = useState([]);
	const [historyId, setHistoryId] = useState(0);
	const [myComment, setMyComment] = useState({
		content: '',
		rating: 0
	});
	const [icons, setIcons] = useState({
		like: 'hearthollow',
		bookmark: 'starhollow',
		star1: 'starhollow',
		star2: 'starhollow',
		star3: 'starhollow',
		star4: 'starhollow',
		star5: 'starhollow'
	});
	const [videoInfo, setVideoInfo] = useState({
		id: '1',
		title: '',
		subtitle: '',
		source: '',
		description: '',
		thumbnail: ''
	});

	const playbackRates = [0.5, 0.75, 1, 1.25, 1.5, 2];
	const [currentSpeed, setCurrentSpeed] = useState(1);
	const [showSpeedPopup, setShowSpeedPopup] = useState(false);

	const userId = window.sessionStorage.getItem('userId'); // Assuming a fixed userId for this example
	const {id} = useParams();

	const toggleFullScreen = useCallback(() => {
		const element = document.getElementById('videoplayer');
		if (document.fullscreenElement) {
			document.exitFullscreen();
		} else {
			element.requestFullscreen();
		}
	}, []);

	const fetchIcons = useCallback(async () => {
		try {
			const [responseLike, responseBookmark] = await Promise.all([
				axios.get(`${serverUri}/api/v1/like/${id}/${userId}`),
				axios.get(`${serverUri}/api/v1/bookmark/${id}/${userId}`)
			]);

			setIcons(prevIcons => ({
				...prevIcons,
				like: responseLike.data ? 'heart' : 'hearthollow',
				bookmark: responseBookmark.data ? 'star' : 'starhollow'
			}));
		} catch (error) {
			console.error('Error fetching icons:', error);
		}
	}, []);

	const fetchVideo = useCallback(async () => {
		try {
			const [responseVideo, responseHistory] = await Promise.all([
				axios.get(`${serverUri}/api/v1/video/${id}`),
				axios.get(`${serverUri}/api/v1/video/${id}/history/${userId}`)
			]);

			console.log('video data', responseVideo);
			console.log('history data', responseHistory);

			if (responseVideo.data) {
				setVideoInfo(responseVideo.data);
			}

			if (responseHistory.data) {
				setHistoryId(responseHistory.data.id);
				setTimestamp(responseHistory.data.timestamp);
				setAlertOpen(true);
			}

			setLoading(false);
		} catch (error) {
			console.error('Error fetching video:', error);
			setLoading(false);
		}
	}, [id, userId]);

	useEffect(() => {
		fetchVideo();
		fetchIcons();
	}, [fetchVideo, fetchIcons]);

	const handleAlertClose = useCallback(() => {
		setAlertOpen(false);
	});

	const handlePlayFromStart = useCallback(() => {
		videoRef.current.seek(timestamp);
		console.log(videoRef.current);
		handleAlertClose();
	});

	const handlePlayFromTimestamp = useCallback(() => {
		videoRef.current.seek(0);
		handleAlertClose();
	});

	const handleLikeToggle = useCallback(async () => {
		try {
			const currentLikeStatus = icons.like === 'heart';
			const response = await axios[currentLikeStatus ? 'delete' : 'post'](
				`${serverUri}/api/v1/like/${id}/${userId}`
			);

			if (response) {
				setIcons(prevIcons => ({
					...prevIcons,
					like: currentLikeStatus ? 'hearthollow' : 'heart'
				}));
				setPopupMessage(
					currentLikeStatus ? '좋아요를 삭제했습니다' : '좋아요를 추가했습니다'
				);
				setPopupOpen(true);
			}
		} catch (error) {
			console.error('Error toggling like:', error);
		}
	}, [icons.like, id, userId]);

	const handleBookmarkToggle = useCallback(async () => {
		try {
			const currentBookmarkStatus = icons.bookmark === 'star';
			const response = await axios[currentBookmarkStatus ? 'delete' : 'post'](
				`${serverUri}/api/v1/bookmark/${id}/${userId}`
			);

			if (response) {
				setIcons(prevIcons => ({
					...prevIcons,
					bookmark: currentBookmarkStatus ? 'starhollow' : 'star'
				}));
				setPopupMessage(
					currentBookmarkStatus
						? '북마크를 삭제했습니다'
						: '북마크를 추가했습니다'
				);
				setPopupOpen(true);
			}
		} catch (error) {
			console.error('Error toggling bookmark:', error);
		}
	}, [icons.bookmark, id, userId]);

	const handleBack = useCallback(async () => {
		const currentTimestamp = Math.trunc(
			videoRef.current.getMediaState().currentTime
		);
		const totalDuration = Math.trunc(videoRef.current.getMediaState().duration);
		console.log('currentTime is', currentTimestamp);
		console.log('totalDuration is', totalDuration);
		try {
			if (currentTimestamp !== totalDuration) {
				if (historyId !== 0) {
					await axios.patch(
						`${serverUri}/api/v1/video/${id}/history/${historyId}/${userId}`,
						currentTimestamp,
						{
							headers: {
								'Content-Type': 'application/json' // 명시적으로 JSON 타입 설정
							}
						}
					);
				} else {
					await axios.post(
						`${serverUri}/api/v1/video/${id}/history/${userId}`,
						currentTimestamp,
						{
							headers: {
								'Content-Type': 'application/json' // 명시적으로 JSON 타입 설정
							}
						}
					);
				}
				navigate(-1);
			} else {
				await axios.delete(
					`${serverUri}/api/v1/video/${id}/history/${historyId}/${userId}`,
					currentTimestamp,
					{
						headers: {
							'Content-Type': 'application/json' // 명시적으로 JSON 타입 설정
						}
					}
				);
			}
		} catch (error) {
			console.error('Error updating history:', error);
		}
	});

	const handlePopupClose = useCallback(() => {
		setTimeout(() => {
			setPopupOpen(false);
		}, 3000);
	}, []);

	const handleCommentToggle = useCallback(async () => {
		try {
			const commentList = await axios.get(`${serverUri}/api/v1/comment/${id}`);
			setComments(commentList.data);
			setCommentOpen(true);
		} catch (error) {
			console.error('Error fetching comments:', error);
		}
	}, [id]);

	const handleRating = useCallback(score => {
		setMyComment(prevInfo => ({...prevInfo, rating: score}));
		setIcons(prevIcons => {
			const newIcons = {...prevIcons};
			for (let i = 1; i <= 5; i++) {
				newIcons[`star${i}`] = i <= score ? 'star' : 'starhollow';
			}
			return newIcons;
		});
	}, []);

	const handleCommentSubmit = useCallback(async () => {
		try {
			if (myComment.content !== '') {
				const response = await axios.post(
					`${serverUri}/api/v1/comment/add/${id}/${userId}`,
					myComment
				);
				if (response) {
					setMyComment({content: '', rating: 0});
					handleRating(0);
					handleCommentToggle();
				}
			}
		} catch (error) {
			console.error('Error submitting comment:', error);
		}
	}, [myComment, id, userId, handleRating, handleCommentToggle]);

	const handleSpeedChange = useCallback(speed => {
		if (videoRef.current) {
			videoRef.current.setPlaybackSpeed(speed);
			setCurrentSpeed(speed);
			setShowSpeedPopup(false);
		}
	}, []);

	const toggleSpeedPopup = useCallback(() => {
		setShowSpeedPopup(prev => !prev);
	}, []);

	if (loading) {
		return <CustomSpinner size="medium" />;
	}

	return (
		<div onClick={() => setPopupOpen(false)}>
			<Popup open={popupOpen} onShow={handlePopupClose} scrimType="transparent">
				<div style={{display: 'flex', justifyContent: 'center'}}>
					{popupMessage}
				</div>
			</Popup>

			<Popup open={commentOpen} position="fullscreen">
				<div style={{display: 'flex', justifyContent: 'flex-start'}}>
					<Button
						icon="arrowhookleft"
						onClick={() => setCommentOpen(false)}
						size="small"
					/>
				</div>
				<div
					style={{
						display: 'flex',
						justifyContent: 'space-between',
						padding: '1rem 30%'
					}}
				>
					{[1, 2, 3, 4, 5].map(i => (
						<Button
							key={i}
							size="large"
							icon={icons[`star${i}`]}
							onClick={() => handleRating(i)}
						/>
					))}
				</div>
				<div
					style={{
						display: 'flex',
						justifyContent: 'center',
						paddingBlock: '2rem'
					}}
				>
					<InputField
						placeholder="한줄평을 입력해주세요"
						value={myComment.content}
						onChange={e => {
							setMyComment(prev => ({...prev, content: e.value}));
						}}
					/>
					<Button onClick={handleCommentSubmit} size="large">
						한줄평 작성
					</Button>
				</div>
				<div style={{width: '100%', height: '100%'}}>
					<Scroller>
						<div>
							{comments.length > 0 ? (
								comments
									.slice(-8)
									.sort((a, b) => b.id - a.id)
									.map(({id, content, rating}) => (
										<div
											key={id}
											style={{
												margin: '10px 10% 10px 20%',
												display: 'flex',
												justifyContent: ''
											}}
										>
											<div style={{display: 'flex', marginLeft: '10px'}}>
												{Array.from({length: rating}, (_, idx) => (
													<Icon key={`star-${idx}`} size="small">
														star
													</Icon>
												))}
												{Array.from({length: 5 - rating}, (_, idx) => (
													<Icon key={`starhollow-${idx}`} size="small">
														starhollow
													</Icon>
												))}
											</div>
											<div style={{paddingLeft: '15rem'}}>
												{content || '내용 없음'}
											</div>
										</div>
									))
							) : (
								<div
									style={{textAlign: 'center', padding: '20px', color: '#888'}}
								>
									등록된 댓글이 없습니다.
								</div>
							)}
						</div>
					</Scroller>
				</div>
			</Popup>
			<div
				style={{
					height: '100vh',
					transform: 'scale(1)',
					transformOrigin: 'top',
					width: '95vw',
					display: 'flex',
					justifyContent: 'center',
					margin: '0 auto'
				}}
			>
				<VideoPlayer
					id="videoplayer"
					ref={videoRef}
					title={videoInfo.title}
					titleHideDelay={4000}
					miniFeedbackHideDelay={2000}
					jumpDelay={200}
					initialJumpDelay={400}
					feedbackHideDelay={3000}
					backButtonAriaLabel="이전 화면으로 돌아갑니다"
					noAutoPlay
					pauseAtEnd
					jumpBy={10}
					playbackRateHash={{
						fastForward: playbackRates,
						slowForward: playbackRates.slice().reverse()
					}}
					poster={videoInfo.thumbnail}
					onBack={handleBack}
				>
					<source src={videoInfo.source} type="video/mp4" />
					<infoComponents>{videoInfo.description}</infoComponents>
					<MediaControls>
						<Button
							icon="create"
							size="small"
							tooltipText="댓글"
							tooltipType="balloon"
							onClick={handleCommentToggle}
						/>
						<Button
							icon="playspeed"
							size="small"
							tooltipText={`재생 속도 (${currentSpeed}x)`}
							tooltipType="balloon"
							onClick={toggleSpeedPopup}
						/>
						<Button
							icon={icons.bookmark}
							size="small"
							tooltipText="북마크"
							tooltipType="balloon"
							onClick={handleBookmarkToggle}
						/>
						<Button
							icon={icons.like}
							size="small"
							tooltipText="좋아요"
							tooltipType="balloon"
							onClick={handleLikeToggle}
						/>
						<Button icon="fullscreen" size="small" onClick={toggleFullScreen} />
					</MediaControls>
				</VideoPlayer>

				<Popup open={showSpeedPopup} onClose={() => setShowSpeedPopup(false)}>
					<div style={{display: 'flex', justifyContent: 'space-evenly'}}>
						{playbackRates.map(speed => (
							<Button
								key={speed}
								onClick={() => handleSpeedChange(speed)}
								selected={speed === currentSpeed}
								size="small"
								style={{margin: '5px'}}
							>
								{speed}x
							</Button>
						))}
					</div>
				</Popup>

				<Alert open={alertOpen} onClose={handleAlertClose} type="overlay">
					이전 시청 기록이 존재합니다.
					<buttons>
						<Button onClick={handlePlayFromStart}>이어서 시청하기</Button>
						<Button onClick={handlePlayFromTimestamp}>처음부터 시청하기</Button>
					</buttons>
				</Alert>
			</div>
		</div>
	);
};

export default Video;
