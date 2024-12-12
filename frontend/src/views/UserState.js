import axios from 'axios';
import {useState, useCallback} from 'react';
import {useNavigate} from 'react-router-dom';
import {InputField} from '@enact/sandstone/Input';
import Button from '@enact/ui/Button';
import {Header, Panel} from '@enact/sandstone/Panels';
const serverUri = process.env.REACT_APP_SERVER_URI;

const UserState = props => {
	const [username, setUserName] = useState('');
	const [age, setAge] = useState(0);
	const [nickname, setNickName] = useState('');
	const [gender, setGender] = useState(null);
	const navigate = useNavigate();
	const styles = {
		genderButton: {
			padding: '10px',
			margin: '5px',
			borderRadius: '5px',
			cursor: 'pointer'
		}
	};

	const handleGender = useCallback(selectedGender => {
		setGender(selectedGender);
	}, []);

	const handleSummit = useCallback(async () => {
		try {
			const response = await axios.post(
				`${serverUri}/api/v1/user/onboarding/${window.sessionStorage.getItem(
					'userId'
				)}`,
				{
					username: username,
					nickname: nickname,
					gender: gender,
					age: age
				}
			);

			const result = await axios.get(
				`${serverUri}/api/v1/user/${window.sessionStorage.getItem(
					'userId'
				)}/onboarding`
			);
			console.log(result.data);
			if (response.data) {
				navigate('/onboarding');
			} else {
				console.log('온보딩 에러');
			}
		} catch (error) {
			console.error('온보딩 오류:', error);
		}
	}, [username, nickname, gender, age, navigate]);
	const handleCloseButton= () => {
		navigate(-1);
	}
	return (
		<Panel
			{...props}
			style={{display: 'flex', flexDirection: 'column', alignContent: 'center'}}
		>
			<Header title={'LGING'} noCloseButton={false} onClose={handleCloseButton}/>
			<div
				style={{
					display: 'flex',
					flexDirection: 'column',
					justifyContent: 'center'
				}}
			>
				<InputField
					placeholder="이름"
					value={username}
					onChange={e => setUserName(e.value)}
				/>

				<InputField
					placeholder="닉네임"
					value={nickname}
					onChange={e => setNickName(e.value)}
				/>

				<InputField
					placeholder="만 나이"
					value={age}
					onChange={e => setAge(e.value)}
				/>
				<div>
					<Button
						style={{
							...styles.genderButton,
							backgroundColor: gender === 'MALE' ? '#007bff' : '#ccc',
							color: gender === 'MALE' ? 'white' : 'black'
						}}
						onClick={() => handleGender('MALE')}
					>
						남성
					</Button>
					<Button
						style={{
							...styles.genderButton,
							backgroundColor: gender === 'FEMALE' ? '#007bff' : '#ccc',
							color: gender === 'FEMALE' ? 'white' : 'black'
						}}
						onClick={() => handleGender('FEMALE')}
					>
						여성
					</Button>
				</div>
				<Button onClick={handleSummit}>다음</Button>
			</div>
		</Panel>
	);
};

export default UserState;
