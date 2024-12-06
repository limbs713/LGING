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
const Main = props => {
	const {openPopup} = usePopup();
	const {procStat = {open: false, onClose: () => {}}} = useProcStat();

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

    

	return (
		<Panel {...props} className={css.netflixBackground}>
			<Header type="compact" css={{header: 'LuckyVickiLogo'}} title="LG OTT" />
			<Scroller>
				<div className={css.bannerWrapper}>
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
				<div className={css.netflixRow}>
					<BodyText className={css.netflixRowTitle}>
						{$L('Popular Content')}
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
