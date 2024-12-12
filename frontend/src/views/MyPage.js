import React, {useState, useEffect} from 'react';
import {Header, Panel} from '@enact/sandstone/Panels';
import BodyText from '@enact/sandstone/BodyText';
import Scroller from '@enact/sandstone/Scroller';
import Button from '@enact/sandstone/Button';
import Icon from '@enact/sandstone/Icon';
import Heading from '@enact/sandstone/Heading';
import ImageItem from '@enact/sandstone/ImageItem';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import $L from '@enact/i18n/$L';
import Spinner from '@enact/sandstone/Spinner';
import css from './Main.module.less';
import styled from 'styled-components';

const serverUri = process.env.REACT_APP_SERVER_URI;

const CustomHeader = styled(Header)`
	.enact_ui_Layout_Layout_layout.enact_sandstone_Panels_Header_titlesRow {
		padding-bottom: 0;
	}
`;
const userId = window.sessionStorage.getItem('userId');
const MyPage = props => {
	const [userInfo, setUserInfo] = useState({name: '', gender: '', age: ''});
	const [bookmarkedVideos, setBookmarkedVideos] = useState([]);
	const [likedVideos, setLikedVideos] = useState([]);
	const [comments, setComments] = useState([]);
	const [loading, setLoading] = useState(true);
	const [icons, setIcons] = useState({
		star1: 'starhollow',
		star2: 'starhollow',
		star3: 'starhollow',
		star4: 'starhollow',
		star5: 'starhollow'
	});
	const navigate = useNavigate();

	const loadLikedVideos = userId => {
		axios
			.get(`${serverUri}/api/v1/user/${userId}/likes`)
			.then(response => {
				setLikedVideos(response.data);
				console.log(response.data);
			})
			.catch(error => console.error('Failed to load likedVideos:', error));
	};

	const loadBookmarkedVideos = userId => {
		axios
			.get(`${serverUri}/api/v1/user/${userId}/bookmarks`)
			.then(response => {
				setBookmarkedVideos(response.data);
				console.log(response.data);
			})
			.catch(error => console.error('Failed to load bookmarkedVideos:', error));
	};

	const loadComments = userId => {
		axios
			.get(`${serverUri}/api/v1/user/${userId}/comments`)
			.then(response => {
				setComments(response.data);
				console.log(response.data);
			})
			.catch(error => console.error('Failed to load comments:', error));
	};

	useEffect(() => {
		if (userId) {
			axios
				.get(`${serverUri}/api/v1/user/${userId}`)
				.then(response => {
					console.log(response);
					setUserInfo(response.data);
					loadLikedVideos(response.data.id);
					loadBookmarkedVideos(response.data.id);
					loadComments(response.data.id);
					setLoading(false);
				})
				.catch(error => console.error('Failed to load user data:', error));
		}
	}, []);

	const handleVideoClick = video => {
		navigate(`/video/${video.Id}`); // Navigate to the video playback page
	};

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
	const handleCloseButton = () => {
		navigate(-1);
	};
	return (
		<Panel {...props} className={css.homeBackground}>
			<Header
				title={'LGING'}
				noCloseButton={false}
				onClose={handleCloseButton}
			/>
			<Scroller className={css.centeredScroller} verticalScrollbar="hidden">
				<div style={{padding: '20px'}}>
					<h2>
						<Icon>profile</Icon>My Page
					</h2>
					<h3 style={{marginBottom: '0px'}}>
						<Icon>info</Icon>User Information
					</h3>
					<Heading showLine></Heading>
					<div
						style={{
							display: 'flex',
							alignItems: 'flex-start',
							flexDirection: 'column',
							paddingBlockStart: '0.5rem'
						}}
					>
						<BodyText>
							<b>이름: </b>
							{`${userInfo.username}`}
						</BodyText>
						<BodyText>
							<b>성별: </b>
							{`${userInfo.gender}`}
						</BodyText>
						<BodyText>
							<b>나이: </b>
							{`${userInfo.age}`}
						</BodyText>
					</div>

					<h3 style={{marginBottom: '0px'}}>
						<Icon>bookmark</Icon>Bookmark
					</h3>
					<Heading showLine></Heading>
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
							{bookmarkedVideos.length > 0 ? (
								<ul>
									{bookmarkedVideos.map(video => (
										<ImageItem
											inline
											key={video.id}
											style={{height: 300, width: 350}}
											label={video.subtitle}
											src={video.thumbnail}
											onClick={() => handleVideoClick(video)}
										>
											{video.title}
										</ImageItem>
									))}
								</ul>
							) : (
								<BodyText>{$L('No bookmarked videos.')}</BodyText>
							)}
						</Scroller>
					</div>

					<h3 style={{marginBottom: '0px'}}>
						<Icon>heart</Icon>Like
					</h3>
					<Heading showLine></Heading>
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
							{likedVideos.length > 0 ? (
								<ul>
									{likedVideos.map(video => (
										<ImageItem
											inline
											key={video.id}
											style={{height: 300, width: 350}}
											label={video.subtitle}
											src={video.thumbnail}
											onClick={() => handleVideoClick(video)}
										>
											{video.title}
										</ImageItem>
									))}
								</ul>
							) : (
								<BodyText>{$L('No liked videos.')}</BodyText>
							)}
						</Scroller>
					</div>

					<h3 style={{marginBottom: '0px'}}>
						<Icon>keyboard</Icon>Comment & Rating
					</h3>
					<Heading showLine></Heading>
					<div
						style={{
							display: 'flex',
							justifyContent: 'space-between',
							paddingBlockStart: '0.5rem'
						}}
					>
						<div>
							{comments.length > 0 ? (
								comments
									.slice(-8)
									.sort((a, b) => b.id - a.id)
									.map(({id, content, rating}) => (
										<div
											key={id}
											style={{
												// margin: '10px 10% 10px 20%',
												display: 'flex'
												// justifyContent: ''
											}}
										>
											<div style={{display: 'flex', marginLeft: '5px'}}>
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
											<div style={{paddingLeft: '10rem', display: 'flex'}}>
												{content || '내용 없음'}
											</div>
										</div>
									))
							) : (
								<div
									style={{textAlign: 'center', padding: '20px', color: '#888'}}
								>
									등록한 댓글이 없습니다.
								</div>
							)}
						</div>
					</div>
				</div>
			</Scroller>
		</Panel>
	);
};

export default MyPage;
