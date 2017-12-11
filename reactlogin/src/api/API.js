const api = process.env.REACT_APP_CONTACTS_API_URL || 'http://localhost:3001'

const headers = {
    'Accept': 'application/json'
};

export const doLogin = (payload) =>
    fetch(`${api}/users/login`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    }).then(res => {
        return res.json()
    })
        .catch(error => {
            console.log("This is error");
            return error;
        });

export const doSignUp = (payload) =>
    fetch(`${api}/users/signUp`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: (payload)
    }).then(res => {
        return res.status;
    })
        .catch(error => {
            console.log("This is error");
            return error;
        });

export const listDirFiles = () =>
    fetch(`${api}/users/listDirFiles`)
        .then(res => res.json())
        .catch(error => {
            console.log("This is error.");
            return error;
        });

export const listDir = (payload) =>
    fetch(`${api}/users/listDir`, {
        method: 'POST',
        body: payload
    }).then(res => {
        return res.status;
    }).catch(error => {
        console.log("This is error");
        return error;
    });



export const uploadFile = (payload) =>
    fetch(`${api}/users/upload`, {
        method: 'POST',
        body: payload
    }).then(res => {
        return res.status;
    }).catch(error => {
        console.log("This is error");
        return error;
    });

export const getImages = () =>
    fetch(`${api}/users/getImg`)
        .then(res => res.json())
        .catch(error => {
            console.log("This is error.");
            return error;
        });

export const doLogout = () =>
    fetch(`${api}/users/logout`)
        .then(res => res.status)
        .catch(error => {
            console.log("This is error.");
            return error;
        });

export const createFolder = (payload) =>
    fetch(`${api}/users/createFolder`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: (payload)
    }).then(res => {
        return res.status;
    }).catch(error => {
        console.log("This is error in create folder");
        return error;
    });

export const createSharedFolder = (payload) =>
    fetch(`${api}/users/createSharedFolder`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: (payload)
    }).then(res => {
        return res.status;
    }).catch(error => {
        console.log("This is error in creating Shared folder");
        return error;
    });

export const deleteFile = (payload) =>
    fetch(`${api}/users/deleteFile`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    }).then(res => {
        return res.status;
    }).catch(error => {
        console.log("This is error in deleting File");
        return error;
    });

export const createGroup = (payload) =>
    fetch(`${api}/users/createGroup`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: (payload)
    }).then(res => {
        return res.status;
    }).catch(error => {
        console.log("This is error in create folder");
        return error;
    });

export const addMembers = (payload) =>
    fetch(`${api}/users/addMembersToGroup`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: (payload)
    }).then(res => {
        return res.status;
    }).catch(error => {
        console.log("This is error in creating Shared folder");
        return error;
    });

