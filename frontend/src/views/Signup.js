import {InputField} from '@enact/sandstone/Input';
import {Header, Panel} from '@enact/sandstone/Panels';
import {useState, useCallback} from 'react';
import Button from '@enact/ui/Button';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';

const serverUri = process.env.REACT_APP_SERVER_URI;

const Signup = props => {
	const navigate = useNavigate();
	const [loginId, setLoginId] = useState('');
	const [password, setPassword] = useState('');

	const handleSummit = useCallback(async () => {
		if (loginId !== '' && password !== '') {
			try {
				const response = await axios.post(`${serverUri}/api/v1/user/signup`, {
					loginId: loginId,
					password: password
				});

				if (response) {
					console.log('회원가입 성공');
					navigate('/');
				} else {
					console.log('회원가입 실패');
				}
			} catch (error) {
				console.error('회원가입 오류:', error);
			}
		}
	}, [loginId, password]);

	const handleLoginIdChange = useCallback(e => {
		setLoginId(e.value);
	}, []);

	const handlePasswordChange = useCallback(e => {
		setPassword(e.value);
	}, []);
	const handleCloseButton= () => {
		navigate(-1);
	}
	return (
		// <Panel style={{display: 'block', width: '100%', maxWidth: '400px', padding: '20px', backgroundColor: 'black', alignContent: 'center'}}>
		<Panel {...props}>
			<Header title={'LGING'} noCloseButton={false} onClose={handleCloseButton}/>
			<div
				style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}
			>
				<InputField
					placeholder="사용자 ID"
					value={loginId}
					onChange={handleLoginIdChange}
					size="large"
					type="text"
					style={{marginBottom: '15px'}}
				/>

				<InputField
					placeholder="사용자 PASSWORD"
					value={password}
					onChange={handlePasswordChange}
					size="large"
					type="password"
					style={{marginBottom: '20px'}}
				/>
				<Button onClick={handleSummit} style={{width: '100%'}}>
					회원정보 설정
				</Button>
			</div>
		</Panel>
	);
};

export default Signup;
