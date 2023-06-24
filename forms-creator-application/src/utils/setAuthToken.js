import axios from 'axios'

const setAuthToken = (token, email) =>{
    if(token){
        axios.defaults.headers.common = {'Authorization': `Bearer ${token}`}
        localStorage.setItem('token', token)
        localStorage.setItem('email', email)
    }
    else{
        delete axios.defaults.headers.common['Authorization']
        localStorage.removeItem('token')
        localStorage.removeItem('email')
    }
}

export default setAuthToken