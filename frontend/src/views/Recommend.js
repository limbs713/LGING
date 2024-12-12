import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Header, Panel} from '@enact/sandstone/Panels';
import ImageItem from '@enact/sandstone/ImageItem';
import IconItem from '@enact/sandstone/IconItem';
import Scroller from '@enact/sandstone/Scroller';
import Icon from '@enact/sandstone/Icon';
import styled from 'styled-components';
import Spinner from '@enact/sandstone/Spinner';
import Alert from '@enact/sandstone/Alert';
import Button from '@enact/sandstone/Button';
import css from './Main.module.less';
import {useNavigate} from 'react-router-dom';

const serverUri = process.env.REACT_APP_SERVER_URI;

const CustomHeader = styled(Header)`
	.enact_ui_Layout_Layout_layout.enact_sandstone_Panels_Header_titlesRow {
		padding-bottom: 0;
	}
`;

const StyledIconItem = styled(IconItem)`
	background: #1b1b1b;
	border-radius: 15px;
	width: 280px;
	height: 180px;

	& > .enact_ui_Layout_Layout_layout {
		align-items: center;
		justify-content: center;
		display: flex;
	}

	& .enact_sandstone_Icon_Icon_icon.enact_sandstone_Icon_Icon_large {
		--icon-size: 4rem;
	}

	& .enact_sandstone_IconItem_IconItem_label {
		font-size: 0.7rem;
	}

	& .enact_sandstone_IconItem_IconItem_iconItem {
		padding-block-end: 3px;
	}
`;

const Recommend = props => {
	const [recommendVideos, setRecommendVideos] = useState([]);
	const [top5ViewedVideos, setTop5ViewedVideos] = useState([]);
	const [targetGroupVideos, setTargetGroupVideos] = useState([]);
	const [latestArchive, setLatestArchive] = useState([]);
	const [archives, setArchives] = useState([]);
	const [loading, setLoading] = useState(true);
	const [showAlert, setShowAlert] = useState(false);
	const [archivePreferenceList, setArchivePreferenceList] = useState(null); // 선택된 archive 저장
	const [selectedArchive, setSelectedArchive] = useState(null);
	const [archiveAlert, setArchiveAlert] = useState(false);
	const [deletePreference, setDeletePreference] = useState(false);
	const userId = window.sessionStorage.getItem('userId');
	const navigate = useNavigate();
	const loadRecommendVideos = () => {
		axios
			.get(`${serverUri}/api/v1/user/recommend/${userId}`)
			.then(response => {
				setRecommendVideos(response.data);
				console.log(response.data);
			})
			.catch(error =>
				console.error('Failed to load recommended videos:', error)
			);
	};

	const loadTop5ViewedVideos = () => {
		axios
			.get(`${serverUri}/api/v1/video/view-top5`)
			.then(response => {
				setTop5ViewedVideos(response.data);
				console.log(response.data);
			})
			.catch(error =>
				console.error('Failed to load top5 viewed videos:', error)
			);
	};

	const loadArchives = () => {
		axios
			.get(`${serverUri}/api/v1/archive/${userId}/all`)
			.then(response => {
				setArchives(response.data);
				console.log(response.data);
			})
			.catch(error => console.error('Failed to load archives:', error));
	};

	const loadTargetGroupVideos = () => {
		axios
			.get(`${serverUri}/api/v1/like/${userId}/target-group`)
			.then(response => {
				setTargetGroupVideos(response.data);
				console.log(response.data);
			})
			.catch(error =>
				console.error('Failed to load target-group videos:', error)
			);
	};

	const loadLatestArchive = () => {
		axios
			.get(`${serverUri}/api/v1/archive/${userId}/latest`)
			.then(response => {
				setLatestArchive(response.data);
				console.log('latestArchive: ', response.data);
			})
			.catch(error => console.error('Failed to load latest archive:', error));
	};

	const onClickVideo = async (videoId, videoSource) => {
		setVideoSource(videoSource);
		console.log('Selected videoId:', videoId);

		try {
			await axios.post(`${serverUri}/api/v1/video/${videoId}/view`);
			navigate(`/video/${videoId}`, {state: {redirectPath: '/recommend'}});
		} catch (error) {
			console.error('Failed to add video views:', error);
		}
	};

	const handleIconItemClick = (archivePreferenceList, archiveId) => {
		setArchivePreferenceList(archivePreferenceList); // 클릭된 archive 저장
		setSelectedArchive(archiveId);
		setShowAlert(true);
		console.log('archivePreferenceList: ', archivePreferenceList);
	};

	const handleAddIconClick = () => {
		setArchiveAlert(true);
		console.log('archiveAlert: ', archiveAlert);
	};

	const handleCloseAddIconClick = () => {
		setArchiveAlert(false);
		navigate('/onboarding', {state: {redirectPath: '/recommend'}});
	};

	const handleCloseAlert = () => {
		setShowAlert(false);
		setArchivePreferenceList(null); // 선택된 archive 초기화
	};

	const handleDeletePreference = async archiveId => {
		setDeletePreference(true);
		console.log('Selected archiveId:', archiveId);

		try {
			await axios.delete(`${serverUri}/api/v1/archive/${userId}/${archiveId}`);
			setArchives(prevArchives =>
				prevArchives.filter(archive => archive.archiveId !== archiveId)
			);
			handleCloseAlert();
		} catch (error) {
			console.error('Failed to delete archive:', error);
		}
	};

	const handleClickVideoEvent = video => {
		console.log("click video is",video);
		onClickVideo(video.videoId, video.source);
	};

	// const handleAddArchiveClick = () => {
	//     navigate('/onboarding')
	// }
	// const [userId, setUserId] = useState(window.sessionStorage.getItem('userId'));
	const [videoID, setVideoID] = useState('');
	const [videoSource, setVideoSource] = useState('');
	const [userData, setUserData] = useState({
		id: 1,
		username: '',
		gender: 0,
		age: 0
	});

	useEffect(() => {
		if (userId) {
			axios
				.get(`${serverUri}/api/v1/user/${userId}`)
				.then(response => {
					setUserData(response.data);
					loadLatestArchive();
					loadArchives();
					loadRecommendVideos();
					loadTargetGroupVideos();
					loadTop5ViewedVideos();
					console.log(recommendVideos)
					setLoading(false);
				})
				.catch(error => console.error('Failed to load user data:', error));
		}
	}, []);

	const generateHashtags = (age, gender) => {
		let ageGroup = '';
		let genderTag = '';

		// 나이대 해시태그 생성
		if (age >= 10 && age < 20) ageGroup = '10대';
		else if (age >= 20 && age < 30) ageGroup = '20대';
		else if (age >= 30 && age < 40) ageGroup = '30대';
		else if (age >= 40 && age < 50) ageGroup = '40대';
		else if (age >= 50) ageGroup = '50대_이상';

		// 성별 해시태그 생성
		if (gender === 'FEMALE') genderTag = '여성';
		else if (gender === 'MALE') genderTag = '남성';

		// 최종 해시태그 조합
		return `#${ageGroup} #${genderTag} #좋아요순`;
	};

	const hashtags = generateHashtags(userData.age, userData.gender);

	if (loading) {
		return (
			<div
				style={{
					display: 'flex',
					justifyContent: 'center',
					alignItems: 'center',
					height: '100vh'
				}}
			>
				<Spinner size="medium" />
			</div>
		);
	}
	const handleCloseButton= () => {
		navigate(-1);
	}
	return (
		<Panel {...props} className={css.homeBackground}>
			<Header title={'LGING'} noCloseButton={false} onClose={handleCloseButton}/>
			{/* // Button들을 수평으로 나열하면 될 듯?
            // 근데 첫번째는 최근 아카이브
            // 두 번째부터 미적용 아카이브를 시간순으로 내림차순
            // 맨 마지막 버튼은 아카이브 추가 버튼
            // 두번째부터 마지막 버튼까지 누르면 수정하시겠습니까 팝업 뜨게함
            // 수정 버튼 누르면 해당 아카이브를 첫 번째 아카이브로 변경 */}
			<Scroller className={css.centeredScroller} verticalScrollbar="hidden">
				<div style={{paddingLeft: '2rem'}}>
					<h2>
						<Icon>list</Icon>Recommend
					</h2>

					<h3>
						<Icon>folder</Icon>개인 취향 아카이브
					</h3>
					<div
						style={{
							display: 'flex', // Flexbox 활성화
							flexDirection: 'row', // 수평 정렬
							paddingBlockStart: '0.5rem',
							gap: '2rem'
						}}
					>
						{archives.slice(0, 1).map(archive => (
							<StyledIconItem
								background="#A50034"
								icon="folder"
								label={
									<>
										{new Date(archive.updatedAt).toISOString().split('T')[0]}
										<br />
										Latest
									</>
								}
								labelColor="light"
								onClick={() =>
									handleIconItemClick(archive.preferenceList, archive.archiveId)
								}
							></StyledIconItem>
						))}

						{archives.slice(1).map(archive => (
							<StyledIconItem
								background="#A50034"
								disabled
								icon="folder"
								label={new Date(archive.updatedAt).toISOString().split('T')[0]}
								labelColor="light"
								onClick={() =>
									handleIconItemClick(archive.preferenceList, archive.archiveId)
								} // 클릭 이벤트 추가
							></StyledIconItem>
						))}

						<StyledIconItem
							background="#1b1b1b"
							icon="plus"
							label="추가"
							labelColor="light"
							onClick={() => handleAddIconClick()}
						></StyledIconItem>

						<Alert
							open={showAlert}
							onClose={handleCloseAlert}
							title="취향 아카이브를 삭제하시겠습니까?"
							type="fullscreen"
						>
							<buttons>
								<Button onClick={() => handleDeletePreference(selectedArchive)}>
									네
								</Button>
								<Button onClick={() => setShowAlert(false)}>아니오</Button>
							</buttons>
							{archivePreferenceList && archivePreferenceList.length > 0
								? `취향 태그: ${archivePreferenceList.join(', ')}`
								: '취향 태그가 없습니다.'}
						</Alert>

						<Alert
							open={archiveAlert}
							onClose={() => handleCloseAddIconClick}
							title="취향을 추가하시겠습니까?"
							type="fullscreen"
						>
							<buttons>
								<Button onClick={handleCloseAddIconClick}>네</Button>
								<Button onClick={() => setArchiveAlert(false)}>아니오</Button>
							</buttons>
						</Alert>
					</div>

					<h3>
						<Icon>index</Icon>개인 취향 맞춤
					</h3>
					<div
						style={{
							display: 'flex',
							justifyContent: 'space-between',
							paddingBlockStart: '0.5rem'
						}}
					>
						<Scroller
							direction="horizontal"
							style={{
								whiteSpace: 'nowrap',
								overflowX: 'hidden' // 가로 스크롤이 가능하도록 설정
							}}
						>
							{recommendVideos.map(video => (
								<ImageItem
									inline
									key={video.id}
									style={{height: 300, width: 350}}
									label={video.subtitle}
									src={video.thumbnail}
									onClick={() => handleClickVideoEvent(video)}
								>
									{video.title}
								</ImageItem>
							))}
						</Scroller>
					</div>
					<h3>
						<Icon>profilecheck</Icon>나이대&성별 맞춤
					</h3>
					<p
						style={{
							color: 'gray',
							display: 'flex',
							justifyContent: 'space-between',
							paddingBlockStart: '0rem',
							paddingInlineStart: '0.6rem'
						}}
					>
						{hashtags}
					</p>
					<div
						style={{
							display: 'flex',
							justifyContent: 'space-between'
						}}
					>
						<Scroller
							direction="horizontal"
							style={{
								whiteSpace: 'nowrap',
								overflowX: 'hidden' // 가로 스크롤이 가능하도록 설정
							}}
						>
							{targetGroupVideos.map(video => (
								<ImageItem
									inline
									key={video.id}
									style={{height: 300, width: 350}}
									label={video.subtitle}
									src={video.thumbnail}
									onClick={() => handleClickVideoEvent(video)}
								>
									{video.title}
								</ImageItem>
							))}
						</Scroller>
					</div>

					<h3>
						<Icon>nowplaying</Icon>조회수순
					</h3>
					<div
						style={{
							display: 'flex',
							justifyContent: 'space-between'
						}}
					>
						<Scroller
							direction="horizontal"
							style={{
								whiteSpace: 'nowrap',
								overflowX: 'hidden' // 가로 스크롤이 가능하도록 설정
							}}
						>
							{top5ViewedVideos.map(video => (
								<ImageItem
									inline
									key={video.id}
									style={{height: 300, width: 350}}
									label={video.subtitle}
									src={video.thumbnail}
									onClick={() => handleClickVideoEvent(video)}
								>
									{video.title}
								</ImageItem>
							))}
						</Scroller>
					</div>
				</div>
			</Scroller>
		</Panel>
	);
};

export default Recommend;
