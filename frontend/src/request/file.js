import {get, post} from "@/request/http";

export const getFileListByType = p => get('/file/listFiles', p);
export const getFileListByPath = p => get('/file/listFiles', p);
export const createFolder = p => post('/file/createFolder', p);
export const deleteFile = p => post('/file/delete', p);
export const renameFile = p => post('/file/rename', p);
export const getFoldTree = p => get('/file/fileTree', p);
export const moveFile = p => post('/file/move', p);
export const shareFile = p => post('/share/shareFile', p);

// 校验分享链接过期时间
export const checkShareLinkEndTime = p => get('/share/checkEndTime', p);
// 校验分享链接是否需要提取码
export const checkShareLinkType = p => get('/share/shareType', p);
// 校验分享链接提取码是否正确
export const checkShareLinkCode = p => get('/share/checkCode', p);
// 获取分享文件列表
export const getShareFileList = p => get('/share/shareFileList', p);
// 保存分享文件
export const saveShareFile = p => post('/share/saveShare', p);

// 获取我已分享的文件列表
export const getMyShareFileList = p => get('/share/shareList', p);
