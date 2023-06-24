import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import axios from 'axios'
import config  from '../config'
import { setAlertWithTimeout } from './alertSlice'

const initialState = {
    responseLoading: false,
    responseError: null,
    responses: [],
    responseLength: 0
}

const responseSlice = createSlice({
    name: 'response',
    initialState,
    reducers: {},
    extraReducers: builder => {
        builder
        .addCase(getAllResponses.fulfilled, (state, action) => {
          state.responseLoading = false
          state.responses = action.payload
          state.responseLength = state.responses.length
        })
        .addCase(getAllResponses.pending, (state) => {
          state.responseLoading = true
          state.responseError = null
        })
        .addCase(getAllResponses.rejected, (state, action) => {
          state.responseLoading = false
          state.responseError = action.payload
        })

        .addCase(deleteResponse.fulfilled, (state, action) => {
          state.responseLoading = false
          state.responses = state.responses.filter(response => response.id !== action.payload)
          state.responseLength = state.responses.length
        })
        .addCase(deleteResponse.pending, (state) => {
          state.responseLoading = true
          state.responseError = null
        })
        .addCase(deleteResponse.rejected, (state, action) => {
          state.responseLoading = false
          state.responseError = action.payload
        })
        .addCase(createResponse.rejected, (state, action) => {
          state.responseError = action.payload
        })
    }
})

export const getAllResponses = createAsyncThunk('response/getAll', async (formId, thunkApi) => {
  try{
    const response = await axios.get(config.serverUrl+'forms/'+formId+'/submissions/')
    return response.data
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export const createResponse = createAsyncThunk('response/create', async({formId, responses}, thunkApi) => {
  try{
    const response = await axios.post(config.serverUrl+'forms/'+formId+'/submissions/', responses)
    thunkApi.dispatch(setAlertWithTimeout('Form submitted successfully... You can go back to home page now.','success'))
    return response.data
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

// delete response
export const deleteResponse = createAsyncThunk('repsonse/delete', async({formId, responseId}, thunkApi) => {
  try{
    await axios.delete(config.serverUrl+'forms/'+formId+'/submissions/'+responseId)
    return responseId
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export default responseSlice.reducer