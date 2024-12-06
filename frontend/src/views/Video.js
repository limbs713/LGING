import {MediaControls} from '@enact/sandstone/MediaPlayer';
import VideoPlayer from '@enact/sandstone/VideoPlayer';
import Button from '@enact/sandstone/Button';
import React, {useState, useRef, useEffect} from 'react';

const Video = ()  => {
	const [isLargeScreen, setIsLargeScreen] = useState(
		document.fullscreenElement ? true : false
	);

	const toggleFullScreen = () => {
		const element = document.getElementById('videoplayer');
		if (document.fullscreenElement) {
			document.exitFullscreen();
			setIsLargeScreen(false);
		} else {
			element.requestFullscreen();
			setIsLargeScreen(true);
		}
	};

	return (
		<div
			style={{
				height: '70vh',
				transform: 'scale(1)',
				transformOrigin: 'top',
				width: '70vw',
				display: 'flex',
				justifyContent: 'center',
				margin: '0 auto'
			}}
		>
			<VideoPlayer id="videoplayer" noAutoPlay={true}>
				<source
					src="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
					type="video/mp4"
				/>
				<infoComponents> 임시 설명 입니다</infoComponents>
				<MediaControls>
					<Button icon="list" size="small" onClick />
					<Button icon="playspeed" size="small" onClick />
					<Button icon="bookmark" size="small" onClick />
					<Button icon="heart" size="small" onClick />
					<Button icon="pen" size="small" onClick />
					<Button
						icon={isLargeScreen ? 'exitfullscreen' : 'fullscreen'}
						size="small"
						onClick={toggleFullScreen}
					/>
				</MediaControls>
			</VideoPlayer>
		</div>
	);
};

export default Video;
