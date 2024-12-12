import {useState} from 'react';
import ThemeDecorator from '@enact/sandstone/ThemeDecorator';
import Panels from '@enact/sandstone/Panels';
import Main from '../views/Main';
import {HashRouter as Router, Routes, Route} from 'react-router-dom';
import Video from '../views/Video';
import {useBackHandler, useCloseHandler, useDocumentEvent} from './AppState';
import {isDevServe} from '../libs/utils';
import Onboarding from '../views/Onboarding';
import Signup from '../views/Signup';
import UserState from '../views/UserState';
import Login from '../views/Login';
import Status from '../views/Status';
import Recommend from '../views/Recommend';
import Videos from '../views/Videos';
import MyPage from '../views/MyPage'

if (isDevServe()) {
	window.webOSSystem = {
		highContrast: 'off',
		close: () => {},
		platformBack: () => {},
		PmLogString: () => {},
		screenOrientation: 'landscape',
		setWindowOrientation: () => {}
	};
}

const App = props => {
	const [skinVariants, setSkinVariants] = useState({highContrast: false});
	const handleBack = useBackHandler();
	const handleClose = useCloseHandler();
	useDocumentEvent(setSkinVariants);

	return (
		<Router>
			<Routes>
				<Route
					path="/"
					element={
						<Panels
							skinVariants={skinVariants}
							onBack={handleBack}
							onClose={handleClose}
						>
							<Login />
						</Panels>
					}
				/>
				<Route path="/signup" element={<Signup />} />
				<Route path="/onboarding" element={<Onboarding />} />
				<Route path="/userstate" element={<UserState />} />
				<Route path="/main" element={<Main />} />
				<Route path="/video/:id" element={<Video />} />
				<Route path="/recommend" element={<Recommend />} />
				<Route path="/status" element={<Status />} />
				<Route path="/videos" element={<Videos />} />
				<Route path="/mypage" element={<MyPage />} />
				<Route path="/status" element={<Status />} />
			</Routes>
		</Router>
	);
};

export default ThemeDecorator(App);
