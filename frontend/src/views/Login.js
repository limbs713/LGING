import {useState} from 'react';
import {InputField} from '@enact/sandstone/Input';
import {Button} from '@enact/sandstone/Button';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import {Header, Panel} from '@enact/sandstone/Panels';

const serverUri = process.env.REACT_APP_SERVER_URI;

const Login = props => {
	const navigate = useNavigate();
	const [loginId, setLoginId] = useState('');
	const [password, setPassword] = useState('');

	const handleSignup = () => {
		navigate('/signup');
	};

	const handleLogin = async () => {
		console.log(loginId, password);
		const response = await axios.post(`${serverUri}/login`, {
			loginId: loginId,
			password: password
		});
		console.log(response);
		window.sessionStorage.setItem('userId', response.data.userId);

		const userInfo = await axios.get(
			`${serverUri}/api/v1/user/${response.data.userId}/onboarding`
		);

		if (userInfo.data) {
			navigate('/main');
		} else {
			navigate('/userState');
		}
	};

	return (
		<Panel {...props} style={{backgroundColor: 'black'}}>
			<Header title={'LGING'} noCloseButton= {false} noBackButton={true}/>
			<div
				style={{
					display: 'flex',
					flexDirection: 'column',
					alignItems: 'center',
					padding: '5%'
				}}
			>
				<p style={{fontSize: '2.5rem'}}>로그인</p>
				<InputField
					placeholder="ID"
					value={loginId}
					onChange={e => setLoginId(e.value)}
					size="large"
					type="text"
					style={{marginBottom: '15px'}}
				/>
				<InputField
					placeholder="PASSWORD"
					value={password}
					onChange={e => setPassword(e.value)}
					size="large"
					type="password"
					style={{marginBottom: '15px'}}
				/>
				<Button onClick={handleLogin}>
					로그인
				</Button>
				<p style={{marginBottom: '20px'}}>or</p>
				<Button onClick={handleSignup}>회원가입</Button>
			</div>
		</Panel>
	);
};

export default Login;
