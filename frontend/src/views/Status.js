import {Doughnut} from 'react-chartjs-2';
import $L from '@enact/i18n/$L';
import BodyText from '@enact/sandstone/BodyText';
import Scroller from '@enact/sandstone/Scroller';
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';
import {useTvInfo, useCpu, useMemory} from '../hooks/useData';
import {useState, useEffect} from 'react';
import Popup from '@enact/sandstone/Popup';
import {Header, Panel} from '@enact/sandstone/Panels';
import {useNavigate} from 'react-router-dom';
ChartJS.register(ArcElement, Tooltip, Legend);

const Status = () => {
	const data = useTvInfo();
	const navigate = useNavigate();
	const [cpuData, setCpuData] = useCpu({returnValue: false});
	const [memoryData, setMemoryData] = useMemory({returnValue: false});
	const [popupOpen, setPopupOpen] = useState(true);
	const [customData, setCustomData] = useState({
		usable_memory: '',
		swapUsed: '',
		cur_vmallocSize: '',
		init_vmallocSize: '',
		user: '',
		system: '',
		nice: '',
		idle: ''
	});

	const renderTVInfo = () => (
		<div>
			<h2>TV System Information</h2>
			{data && (
				<ul>
					<li>Firmware Version: {data.firmwareVersion}</li>
					<li>Model Name: {data.modelName}</li>
					<li>SDK Version: {data.sdkVersion}</li>
					<li>UHD: {data.UHD}</li>
				</ul>
			)}
		</div>
	);

	const setData = async () => {
		setMemoryData();
		setCpuData();
	};

	useEffect(() => {
		setData();
	}, []);

	useEffect(() => {
		if (cpuData.returnValue) {
			const statusList = cpuData.stat || [];
			console.log(statusList);
			const cpuInfo = statusList.find(line => line.startsWith('cpu '));
			console.log(cpuInfo);

			if (cpuInfo) {
				const [, user, system, nice, idle] = cpuInfo.split(/\s+/).map(Number);
				console.log(user + ', ' + system + ', ' + nice + ', ' + idle);
				setCustomData({
					...customData,
					user: user,
					system: system,
					nice: nice,
					idle: idle
				});
			} else {
				setCustomData({
					...customData,
					user: 'NaN',
					system: 'NaN',
					nice: 'NaN',
					idle: 'NaN'
				});
			}
		}
	}, [cpuData]);

	useEffect(() => {
		if (memoryData.returnValue) {
			const swapUsed = memoryData.swapUsed || 0;
			const usableMemory = memoryData.usable_memory || 0;

			const vmallocInfo = memoryData.vmallocInfo || {};
			const current_vmallocSize = vmallocInfo.cur_vmallocSize || 0;
			const initial_vmallocSize = vmallocInfo.init_vmallocSize || 0;

			setCustomData({
				...customData,
				usable_memory: usableMemory,
				swapUsed: swapUsed,
				cur_vmallocSize: current_vmallocSize,
				init_vmallocSize: initial_vmallocSize
			});
		}
	}, [memoryData]);

	const MemDoughnutData = {
		labels: ['usable_memory', 'swapUsed', 'cur_vmallocSize'],
		datasets: [
			{
				label: 'Memory Data',
				data: [
					customData.usable_memory,
					customData.swapUsed,
					customData.cur_vmallocSize
				],
				backgroundColor: [
					'rgba(255, 99, 132, 0.3)',
					'rgba(54, 162, 235, 0.3)',
					'rgba(255, 206, 86, 0.3)'
				],
				borderColor: ['gray'],
				borderWidth: 0.5
			}
		]
	};

	const CpuDoughnutData = {
		labels: ['user', 'system', 'nice', 'idle'],
		datasets: [
			{
				label: 'CPU share',
				data: [
					customData.user,
					customData.system,
					customData.nice,
					customData.idle
				],
				backgroundColor: [
					'rgba(255, 99, 132, 0.2)',
					'rgba(54, 162, 235, 0.2)',
					'rgba(255, 206, 86, 0.2)',
					'rgba(75, 192, 192, 0.2)'
				],
				borderColor: ['gray'],
				borderWidth: 0.5
			}
		]
	};

	const handlePopup = () => {
		setPopupOpen(false);
	};
	const handleCloseButton = () => {
		navigate(-1);
	};
	return (
		<Panel>
			<Header
				title={'리소스 사용량'}
				noCloseButton={false}
				onClose={handleCloseButton}
			/>
			<Scroller direction="vertical">
				<h2>{$L('System Status')}</h2>
				{renderTVInfo()}
				{/* {<Button onClick={setData} size="small" className={css.buttonCell}>
                {$L('Refresh')}
            </Button>} */}
				<Popup open={popupOpen} onClick={handlePopup}>
					<BodyText>{`Cpu status : ${JSON.stringify(cpuData)}`}</BodyText>
					<BodyText>{`Mem status: ${JSON.stringify(memoryData)}`}</BodyText>
				</Popup>
				<div
					style={{
						position: 'absolute',
						top: '150px',
						left: '0px',
						height: '600px',
						width: '700px'
					}}
				>
					<Doughnut data={MemDoughnutData} />
					<div
						style={{
							top: '800px',
							left: '90px'
						}}
					>
						<h3>{`현재 가상 메모리 크기: ${customData.cur_vmallocSize}`}</h3>
						<h3>{`초기 가상 메모리 크기: ${customData.init_vmallocSize}`}</h3>
						<h3>{`사용된 스왑 메모리: ${customData.swapUsed}`}</h3>
						<h3>{`사용 가능한 메모리: ${customData.usable_memory}`}</h3>
					</div>
				</div>
				<div
					style={{
						position: 'absolute',
						top: '150px',
						left: '700px',
						height: '600px',
						width: '700px'
					}}
				>
					<Doughnut data={CpuDoughnutData} />
					<div
						style={{
							position: 'absolute',
							top: '800px',
							left: '850px'
						}}
					>
						<h3>{`사용자 CPU 사용률: ${customData.user}`}</h3>
						<h3>{`시스템 CPU 사용률: ${customData.system}`}</h3>
						<h3>{`NICE 프로세스 사용률: ${customData.nice}`}</h3>
						<h3>{`CPU 대기 상태 비율: ${customData.idle}`}</h3>
					</div>
				</div>
			</Scroller>
		</Panel>
	);
};

export default Status;
