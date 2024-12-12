// This is subscribe APIs.
import {useEffect, useRef, useState} from 'react';

import debugLog from '../libs/log';
import {mem} from '../libs/services';
import LS2Request from '@enact/webos/LS2Request';
import {getSystemInfo} from '../libs/services';

// example:
//  luna://com.webos.memorymanager/getProcStat '{"subscribe":true}'
var webOS_LS2Request = new LS2Request();

export const useProcStat = () => {
	const ref = useRef(null);
	const [value, setValue] = useState({returnValue: false});

	useEffect(() => {
		if (!ref.current) {
			debugLog('GET_PROC_STAT[R]', {});
			ref.current = mem({
				method: 'getProcStat',
				parameters: {
					subscribe: true
				},
				onSuccess: res => {
					debugLog('GET_PROC_STAT[S]', res);
					setValue(res);
				},
				onFailure: err => {
					debugLog('GET_PROC_STAT[F]', err);
				}
			});
		}

		return () => {
			if (ref.current) {
				ref.current.cancel();
				ref.current = null;
			}
		};
	}, []);

	return value;
};

export const useUnitList = () => {
	const ref = useRef(null);
	const [value, setValue] = useState({returnValue: false});

	useEffect(() => {
		if (!ref.current) {
			debugLog('GET_UNIT_LIST[R]', {});
			ref.current = mem({
				method: 'getUnitList',
				parameters: {
					subscribe: true
				},
				onSuccess: res => {
					debugLog('GET_UNIT_LIST[S]', res);
					setValue(res);
				},
				onFailure: err => {
					debugLog('GET_UNIT_LIST[F]', err);
				}
			});
		}

		return () => {
			if (ref.current) {
				ref.current.cancel();
				ref.current = null;
			}
		};
	}, []);

	return value;
};

export const useTvInfo = () => {
	const ref = useRef(null);
	const [value, setValue] = useState({returnValue: false});

	useEffect(() => {
		if (!ref.current) {
			debugLog('GET_CONFIGS[R]', {});
			ref.current = getSystemInfo({
				parameters: {
					subscribe: true,
					keys: ['firmwareVersion', 'modelName', 'sdkVersion', 'UHD']
				},
				onSuccess: res => {
					debugLog('GET_CONFIGS[S]', res);
					setValue(res);
				},
				onFailure: err => {
					debugLog('GET_CONFIGS[F]', err);
					setValue(err);
				}
			});
		}

		return () => {
			if (ref.current) {
				ref.current.cancel();
				ref.current = null;
			}
		};
	}, []);

	return value;
};

export const useMemory = () => {
	const [value, setValue] = useState({returnValue: false});
	var params = {
		subscribe: true
	};

	return [
		value,
		event => {
			var requestBody = {
				service: 'luna://com.webos.memorymanager',
				method: 'getUnitList',
				parameters: params,
				onSuccess: res => {
					debugLog('Success to get useMemory: ', res);
					setValue(res);
				},
				onFailure: err => {
					debugLog('Failed to get useMemory: ', err);
					setValue('Failed to getUnitList' + JSON.stringify(err));
				}
			};
			webOS_LS2Request.send(requestBody);
		}
	];
};

export const useCpu = () => {
	const [value, setValue] = useState({returnValue: false});

	return [
		value,
		event => {
			var requestBody = {
				service: 'luna://com.webos.memorymanager',
				method: 'getProcStat',
				parameters: {
					subscribe: true
				},
				onSuccess: res => {
					debugLog('Success to get useCpu: ', res);
					setValue(res);
				},
				onFailure: err => {
					debugLog('Failes to get useCpu: ', err);
					setValue('Failed to getProcState' + JSON.stringify(err));
				}
			};
			webOS_LS2Request.send(requestBody);
		}
	];
};
