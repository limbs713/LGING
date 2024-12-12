import ImageItem from '@enact/sandstone/ImageItem';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import {Header, Panel} from '@enact/sandstone/Panels';
import Spinner from '@enact/sandstone/Spinner';
import css from './Main.module.less';
import {useState, useEffect} from 'react';
import {TabLayout, Tab} from '@enact/sandstone/TabLayout';
import Scroller from '@enact/sandstone/Scroller';

const serverUri = process.env.REACT_APP_SERVER_URI;

const Videos = props => {
	const [videos, setVideos] = useState([]);
	const [loading, setLoading] = useState(true);
	const navigate = useNavigate();

	const loadVideos = () => {
		axios
			.get(`${serverUri}/api/v1/video`)
			.then(response => {
				setVideos(response.data);
				console.log(response.data);
			})
			.catch(error => console.error('Failed to load all videos:', error));
	};

	useEffect(() => {
		loadVideos();
		setLoading(false);
	}, []);

	const handleVideoClick = videoId => {
		navigate(`/video/${videoId}`); // Navigate to the video playback page
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

	const handleCloseButton= () => {
		navigate(-1);
	}

	return (
		<Panel {...props} className={css.homeBackground}>
			<Header title={'LGING'} subtitle="PlayList" noCloseButton={false} onClose={handleCloseButton}/>
			<TabLayout>
				<Tab icon="list" title="All Videos">
					<Scroller>
						<div
							style={{
								display: 'grid',
								gridTemplateColumns: 'repeat(3, 1fr)', // 3열로 배치
								gap: '1rem', // 항목 간 간격
								paddingBlockStart: '0.5rem'
							}}
						>
							{videos.map(video => (
								<ImageItem
									inline
									key={video.videoId}
									style={{height: 300, width: '100%'}}
									label={video.subtitle}
									src={video.thumbnail}
									onClick={() => handleVideoClick(video.videoId)}
								>
									{video.title}
								</ImageItem>
							))}
						</div>
					</Scroller>
				</Tab>
			</TabLayout>
		</Panel>
	);
};
export default Videos;
