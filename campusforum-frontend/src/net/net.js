import axios from "axios";
import {ElMessage} from "element-plus";

const authItemName = 'access_token';

const defaultFailure = (message, code, url) => {
    console.warn('请求地址：' + url + '，状态码：' + code + '，错误信息：' + message);
    ElMessage.warning(message);
}

const defaultError = (err) => {
    console.error(err);
    ElMessage.warning("发生了未知错误，请联系管理员");
}

function storeAccessToken(token, remember, expire) {
    const authObj = {
        token: token,
        expire: expire
    }
    const str = JSON.stringify(authObj);
    if (remember)
        localStorage.setItem(authItemName, str);
    else
        sessionStorage.setItem(authItemName, str);
}

function  takeAccessToken() {
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName);
    if (str === null)
        return null;
    const authObj = JSON.parse(str);
    if (authObj.expires <= new Date()) {
        deleteAccessToken();
        ElMessage.warning('登录已过期，请重新登录');
        return null;
    }
    return authObj.token;
}

function deleteAccessToken() {
    localStorage.removeItem(authItemName);
    sessionStorage.removeItem(authItemName);
}

function accessHeader() {
    const token = takeAccessToken();
    return token ? {
        'Authorization': 'Bearer ' + takeAccessToken()
    } : {}
}

function internalPost(url, data, header, success, failure, error = defaultError) {
  axios.post(url, data, { headers: header }).then(({data}) => {
      if (data.code === 200) {
          success(data.data);
      } else {
          failure(data.message, data.code, url);
      }
  }).catch(err => error(err));
}

function internalGet(url, header, success, failure, error = defaultError) {
    axios.get(url, { headers: header }).then(({data}) => {
        if (data.code === 200) {
            success(data.data);
        } else {
            failure(data.message, data.code, url);
        }
    }).catch(err => error(err));
}

function get(url, success, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure);
}

function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data, accessHeader(), success, failure);
}

function login(email, password, remember, success, failure = defaultFailure) {
    internalPost('/api/auth/login', {
        email: email,
        password: password,
    }, {
        'Content-Type': 'application/x-www-form-urlencoded'
    }, (data) => {
        storeAccessToken(data.token, remember, data.expire);
        ElMessage.success(`登录成功, 欢迎${data.username}`);
        success(data);
    }, failure)
}

function logout(success, failure = defaultFailure) {
    get('/api/auth/logout', () => {
        deleteAccessToken();
        ElMessage.success('登出成功');
        success();
    }, failure);
}

function unauthorized() {
    return !takeAccessToken()
}

export { login, logout, get, post, unauthorized }