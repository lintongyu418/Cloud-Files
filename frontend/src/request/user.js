import {get, post} from './http'

// 用户登录
export const login = p => get('/user/login', p);
// 获取登录状态及用户信息
export const checkUserLoginInfo = p => get('/user/check', p);
// 用户注册
export const addUser = p => post('/user/create', p);
