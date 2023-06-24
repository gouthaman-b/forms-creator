import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import axios from 'axios'
import config  from '../config'
import setAuthToken from '../utils/setAuthToken'
import { setAlertWithTimeout } from './alertSlice'

const initialState = {
    token: null,
    email: null,
    authLoading: false,
    authError: null,
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers:{
    logout: (state) => {
        state.token = null
        state.email = null
        state.authLoading = false
        state.authError = null
        setAuthToken()
    },
    loadUserFulFilled: (state, action) => {
      state.token = action.payload.token
      state.email = action.payload.email
      state.authLoading = false
      state.authError = null
    },
    loadUserRejected: (state ) => {
      state.token = null
      state.email = null
      state.authLoading = false
      state.authError = null
    }
  },
  extraReducers: builder => {
    builder
    .addCase(login.pending, (state) => {
        state.authLoading = true
        state.authError = null
    })
    .addCase(login.fulfilled, (state, action) => {
      state.authLoading = false
      state.token = action.payload.token
      state.email = action.payload.email
      setAuthToken(action.payload.token,action.payload.email)
    })
    .addCase(login.rejected, (state, action) => {
      state.authLoading = false
      state.authError = action.payload
    })
    .addCase(register.pending, (state) => {
      state.authLoading = true
      state.authError = null
    })
    .addCase(register.fulfilled, (state, action) => {
      state.authLoading = false
      state.token = action.payload.token
      state.email = action.payload.email
      setAuthToken(action.payload.token,action.payload.email)
    })
    .addCase(register.rejected, (state, action) => {
      state.authLoading = false
      state.authError = action.payload
    })
  }
})

export const login = createAsyncThunk('auth/login', async (credentials, thunkApi) => {
  try{
    const response = await axios.post(config.serverUrl+'auth/login', credentials)
    return {"token":response.data.token, "email":credentials.email}
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data? error.response.data.message : error.response,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }  
})
  
export const register = createAsyncThunk('auth/register', async (credentials, thunkApi) => {
  try{
    const response = await axios.post(config.serverUrl+'auth/register', credentials)
    return {"token":response.data.token, "email":credentials.email}
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data? error.response.data.message : error.response,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export const loadUser = () => dispatch => {
  if(localStorage.getItem('token')){
    setAuthToken(localStorage.getItem('token'), localStorage.getItem('email'))
    dispatch(loadUserFulFilled({"token":localStorage.getItem('token'), "email":localStorage.getItem('email')}))
  }else{
    setAuthToken()
    dispatch(loadUserRejected())
  }
  
}

export const { logout, loadUserFulFilled, loadUserRejected } = authSlice.actions

export default authSlice.reducer
