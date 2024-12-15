import {Header, Panel} from '@enact/sandstone/Panels';
import css from './Main.module.less';
import Scroller from '@enact/sandstone/Scroller';
import MediaOverlay from '@enact/sandstone/MediaOverlay';
import IconItem from '@enact/sandstone/IconItem';
import styled from 'styled-components';
import {useNavigate} from 'react-router-dom';

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
	const navigate = useNavigate();
	return (
		<Panel {...props} className={css.homeBackground}>
			<Scroller className={css.centeredScroller} verticalScrollbar="hidden">
				<div>
					<CustomHeader type="compact" title="LGING" />
					<MediaOverlay
						className={css.fullScreenOverlay}
						css={{
							image: css.bannerVideo,
							textLayout: css.bannerText
						}}
						text="다양한 영상을 추천받으세요.\n당신의 취향대로"
						mediaComponent="video"
						loop
						noAutoPlay={false}
						muted
					>
						<source
							src="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
							type="video/mp4"
						/>
					</MediaOverlay>
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
							onClick={() => navigate('/recommend')}
						></StyledIconItem>
						<StyledIconItem
							icon="list"
							label="VIDEOS"
							onClick={() => navigate('/videos')}
						></StyledIconItem>
						<StyledIconItem
							icon="dashboard1"
							label="STATUS"
							onClick={() => navigate('/status')}
						></StyledIconItem>
						<StyledIconItem
							icon="profile"
							label="MYPAGE"
							onClick={() => navigate('/mypage')}
						></StyledIconItem>
					</div>
				</div>
			</Scroller>
		</Panel>
	);
};

export default Main;
