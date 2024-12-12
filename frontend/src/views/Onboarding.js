import {useState, useCallback} from 'react';
import {Button} from '@enact/sandstone/Button';
import {useLocation, useNavigate} from 'react-router-dom';
import axios from 'axios';
import Popup from '@enact/sandstone/Popup';
import {Header, Panel} from '@enact/sandstone/Panels';

const serverUri = process.env.REACT_APP_SERVER_URI;
const genres = [
	'Action',
	'Comedy',
	'Drama',
	'SF',
	'Romance',
	'Thriller',
	'Horror',
	'Animation',
	'Crime',
	'Adventure'
];

const axiosConfig = {
	headers: {
		'Content-Type': 'application/json'
	}
};
const Onboarding = props => {
	const [popupVisible, setPopupVisible] = useState(false);
	const navigate = useNavigate();
	const location = useLocation();
	const redirectPath = location.state?.redirectPath;

	const [selectedGenre, setSelectedGenre] = useState(
		Object.fromEntries(genres.map(genre => [genre, 0.0]))
	);
	const userId = window.sessionStorage.getItem('userId');

	const chunkArray = (arr, chunkSize) => {
		const chunks = [];
		for (let i = 0; i < arr.length; i += chunkSize) {
			chunks.push(arr.slice(i, i + chunkSize));
		}
		return chunks;
	};

	const genreChunks = chunkArray(genres, 5);

	const handleSelectGenre = useCallback(genre => {
		setSelectedGenre(prevGenres => {
			const countOfSelected = Object.values(prevGenres).filter(
				value => value === 1.0
			).length;

			if (prevGenres[genre] === 0.0 && countOfSelected < 4) {
				console.log(genre, '가 선택되었습니다.');
				return {
					...prevGenres,
					[genre]: 1.0
				};
			}

			if (prevGenres[genre] === 1.0) {
				console.log(genre, '가 선택 해제되었습니다.');
				return {
					...prevGenres,
					[genre]: 0.0
				};
			}

			return prevGenres;
		});
	});

	const handleSubmit = useCallback(async () => {
		console.log('선택한 장르는', selectedGenre);
		const selectedCount = Object.values(selectedGenre).filter(
			value => value === 1.0
		).length;
		if (selectedCount === 4) {
			try {
				const response = await axios.post(
					`${serverUri}/api/v1/user-genre/onboarding/${userId}`,
					{genrePreferences: selectedGenre}
				);
				console.log('Server Response:', response.data);
				await axios.post(
					`${serverUri}/api/v1/archive/${userId}`,
					response.data.vectorId,
					axiosConfig
				);
				console.log(redirectPath);
				if (response && !redirectPath) {
					setPopupVisible(true);
					setTimeout(() => {
						setPopupVisible(false);
						navigate('/');
					}, 2000); // 2초 동안 Popup 출력
				} else navigate(-1);
			} catch (error) {
				console.error('Error:', error);
			}
		} else {
			console.log('정확히 4개의 장르를 선택해주세요.');
		}
	}, [selectedGenre, userId, navigate]);
	const handleCloseButton= () => {
		navigate(-1);
	}
	return (
		<Panel {...props}>
			<div>
			<Header title={'LGING'} noCloseButton={false} onClose={handleCloseButton}/>
				<Popup open={popupVisible} position="center">
					<p>회원가입이 완료되었습니다.</p>
					<br />
					<p>잠시 후 로그인 화면으로 이동합니다.</p>
				</Popup>
				{/* 취향 입력 */}
				<h3> 4개의 취향을 선택해주세요 </h3>
				<div>
					{genreChunks.map((chunk, index) => (
						<div key={index} style={{paddingBlockEnd: '2rem'}}>
							{chunk.map((g, i) => (
								<Button
									key={i}
									size="large"
									style={{
										backgroundColor:
											selectedGenre[g] === 1.0 ? '#007bff' : '#ccc',
										color: selectedGenre[g] === 1.0 ? 'white' : 'black'
									}}
									onClick={() => handleSelectGenre(g)}
								>
									{g}
								</Button>
							))}
						</div>
					))}
					<Button size="large" onClick={handleSubmit}>
						선택 완료
					</Button>
				</div>
			</div>
		</Panel>
	);
};

export default Onboarding;
