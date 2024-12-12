import Alert from '@enact/sandstone/Alert';
import BodyText from '@enact/sandstone/BodyText';
import {Header, Panel} from '@enact/sandstone/Panels';
import {usePopup} from './MainState';

import css from './Main.module.less';
import $L from '@enact/i18n/$L';
import {useProcStat} from '../hooks/useData';

import Scroller from '@enact/sandstone/Scroller';
import Item from '@enact/sandstone/Item';
import VirtualGridList from '@enact/sandstone/VirtualList';
import ImageItem from '@enact/sandstone/ImageItem';
import MediaOverlay from '@enact/sandstone/MediaOverlay';
import IconItem from '@enact/sandstone/IconItem';
import styled from 'styled-components';
import {useNavigate} from 'react-router-dom';

	const handlePlayClick = () => {
		openPopup('play');
	};

	const handleInfoClick = () => {
		openPopup('info');
	};

	const renderItem = ({index, ...rest}) => (
		<ImageItem
			{...rest}
			src={`https://picsum.photos/300/450?random=${index}`}
			caption={$L('Movie {index}', {index: index + 1})}
		/>
	);

    

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
					<CustomHeader
						type="compact"
						title="LGING"
					/>
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
				<div className={css.netflixRow}>
					<BodyText className={css.netflixRowTitle}>
						{$L('Trending Now')}
					</BodyText>
					<VirtualGridList
						dataSize={20}
						itemRenderer={renderItem}
						itemSize={450}
						horizontalScrollbar="hidden"
						direction="horizontal"
					/>
				</div>
				<div className={css.netflixRow}>
					<BodyText className={css.netflixRowTitle}>
						{$L('Only on Netflix')}
					</BodyText>
					<VirtualGridList
						dataSize={20}
						itemRenderer={renderItem}
						itemSize={450}
						horizontalScrollbar="hidden"
						direction="horizontal"
					/>
				</div>
			</Scroller>

			<Alert open={procStat.open} onClose={procStat.onClose}>
				{$L('Process completed successfully!')}
			</Alert>
		</Panel>
	);
};

export default Main;
