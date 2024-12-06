import {Header, Panel} from '@enact/sandstone/Panels';

import css from './Main.module.less';

import Scroller from '@enact/sandstone/Scroller';
import MediaOverlay from '@enact/sandstone/MediaOverlay';
import IconItem from '@enact/sandstone/IconItem';
import styled from 'styled-components';
import Video from '../views/Video';

const CustomHeader = styled(Header)`
	.enact_ui_Layout_Layout_layout.enact_sandstone_Panels_Header_titlesRow {
		padding-bottom: 0;
	}
`;

const StyledIconItem = styled(IconItem)`
	background: #1b1b1b;
	border-radius: 15px;
	width: 240px;
	height: 180px;

	& > .enact_ui_Layout_Layout_layout {
		display: flex;
		justify-content: space-around;
		align-items: center;
		padding-block-end: 1rem;
	}

	& .enact_sandstone_Icon_Icon_icon.enact_sandstone_Icon_Icon_large {
		--icon-size: 5rem;
	}

	& .enact_sandstone_IconItem_IconItem_label {
		font-size: 1.25rem;
	}

	& .enact_sandstone_IconItem_IconItem_iconItem {
		padding-block-end: 10px;
	}
`;

const Main = props => {
	return (
		<Panel {...props} className={css.homeBackground}>
			<Scroller className={css.centeredScroller} verticalScrollbar="hidden">
				<div>
					<CustomHeader type="compact" title="LG OTT" />
					<MediaOverlay
						className={`${css.fullScreenOverlay} ${css.mediaOverlay}`}
						css={{
							image: css.bannerVideo
						}}
						mediaComponent="video"
						loop
						noAutoPlay={false}
						muted
					>
						<source
							src="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
							type="video/mp4"
							height="150"
							width="50"
						/>
					</MediaOverlay>
					<div className={css.overlayContent}>
						<h1 className={css.mainTitle}>WebOs에서 펼쳐지는</h1>
						<h1 className={css.mainTitle}>자유로운 Media 시청</h1>
						<p className={css.subtitle}>
							아카이브를 통해 취향에 맞는 영상을 추천받으세요.{' '}
						</p>
					</div>
				</div>
				<div
					style={{
						padding: '2.375rem',
						paddingTop: '0.5rem'
					}}
				>
					<h2>다양한 기능을 사용해보세요</h2>
					<div
						style={{
							display: 'flex',
							justifyContent: 'space-between',
							paddingBlockStart: '1rem'
						}}
					>
						<StyledIconItem
							icon="playcircle"
							label="RECOMMEND"
						></StyledIconItem>
						<StyledIconItem
							icon="list"
							label="VIDEOS"
						></StyledIconItem>
						<StyledIconItem
							icon="dashboard1"
							label="STATUS"
						></StyledIconItem>
						<StyledIconItem
							icon="profile"
							label="MYPAGE"
						></StyledIconItem>
					</div>
				</div>
			</Scroller>
		</Panel>
	);
};

export default Main;
