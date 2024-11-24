/**
 * 定义了一个字符串常量 SESSION_ALL_TRAIN，用作 sessionStorage 的键名，用于存储和读取“所有车次”的数据。
 * Created By Zhangjilin 2024/11/24
 * @type {string}
 */
SESSION_ALL_TRAIN = "SESSION_ALL_TRAIN";

SessionStorage = {
    get: function (key) {
        var v = sessionStorage.getItem(key);
        if (v && typeof(v) !== "undefined" && v !== "undefined") {
            return JSON.parse(v);
        }
    },
    set: function (key, data) {
        sessionStorage.setItem(key, JSON.stringify(data));
    },
    remove: function (key) {
        sessionStorage.removeItem(key);
    },
    clearAll: function () {
        sessionStorage.clear();
    }
};