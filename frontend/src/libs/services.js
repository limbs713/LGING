import request from '../libs/request';

const sys = request('luna://com.webos.service.tv.systemproperty');
export const getSystemInfo = params =>
	sys({method: 'getSystemInfo', ...params});

export const mem = callInfo =>
	request('luna://com.webos.memorymanager')(callInfo);

export const sam = callInfo =>
	request('luna://com.webos.applicationManager')(callInfo);
