import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import axios from 'axios'
import config  from '../config'
import { setAlertWithTimeout } from './alertSlice'
import { parse } from 'date-fns'
import { createQuestion } from './questionSlice'


const initialState = {
    form: null,
    forms: [],
    formLoading: false,
    formError: null
}

const formSlice = createSlice({
    name: 'form',
    initialState,
    reducers: {},
    extraReducers: builder => {
      builder
      .addCase(getAllForms.fulfilled, (state, action) => {
        state.formLoading = false
        state.forms = action.payload
        state.forms.sort((a,b) => parse(b.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()) - parse(a.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()))
      })
      .addCase(getAllForms.pending, (state) => {
        state.formLoading = true
        state.formError = null
      })
      .addCase(getAllForms.rejected, (state, action) => {
        state.formLoading = false
        state.formError = action.payload
      })

      .addCase(getOneForm.fulfilled, (state, action) => {
        state.formLoading = false
        state.form = action.payload
      })
      .addCase(getOneForm.pending, (state) => {
        state.formLoading = true
        state.formError = null
      })
      .addCase(getOneForm.rejected, (state, action) => {
        state.formLoading = false
        state.formError = action.payload
      })

      .addCase(createForm.fulfilled, (state, action) => {
        state.formLoading = false
        state.forms.push(action.payload)
        state.forms.sort((a,b) => parse(b.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()) - parse(a.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()))
      })
      .addCase(createForm.pending, (state) => {
        state.formLoading = true
        state.formError = null
      })
      .addCase(createForm.rejected, (state, action) => {
        state.formLoading = false
        state.formError = action.payload
      })

      .addCase(updateForm.fulfilled, (state, action) => {
        state.formLoading = false
        state.form = action.payload
        state.forms = state.forms.map(form => form.id === action.payload.id ? action.payload : form)
        state.forms.sort((a,b) => parse(b.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()) - parse(a.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()))
      })
      .addCase(updateForm.pending, (state) => {
        state.formLoading = true
        state.formError = null
      })
      .addCase(updateForm.rejected, (state, action) => {
        state.formLoading = false
        state.formError = action.payload
      })

      .addCase(deleteForm.fulfilled, (state, action) => {
        state.formLoading = false
        state.forms = state.forms.filter(form => form.id !== action.payload)
        state.forms.sort((a,b) => parse(b.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()) - parse(a.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()))
      })
      .addCase(deleteForm.pending, (state) => {
        state.formLoading = true
        state.formError = null
      })
      .addCase(deleteForm.rejected, (state, action) => {
        state.formLoading = false
        state.formError = action.payload
      })
      

    }
})

export const getAllForms = createAsyncThunk('form/getAll', async (dummy, thunkApi) => {
    try{
      const response = await axios.get(config.serverUrl+'forms/')
      return response.data
    }catch(error){
      thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
      return thunkApi.rejectWithValue(error.response.data)
    }  
})

export const createForm = createAsyncThunk('form/create', async (dummy, thunkApi) => {
  try{
    const form = {
      'title': 'Untitled Form',
      'description': 'Description of form',
      'enabled': false
    }
    const response = await axios.post(config.serverUrl+'forms/', form)
    thunkApi.dispatch(createQuestion(response.data.id))
    return response.data
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export const getOneForm = createAsyncThunk('form/getOne', async (formId, thunkApi) => {
    try{
      const response = await axios.get(config.serverUrl+'forms/'+formId)
      return response.data
    }catch(error){
      thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
      return thunkApi.rejectWithValue(error.response.data)
    }  
})

export const updateForm = createAsyncThunk('form/update', async (form, thunkApi) => {
  try{
    const response = await axios.put(config.serverUrl+'forms/'+form.id, form)
    thunkApi.dispatch(setAlertWithTimeout('Saving changes to form','success'))
    return response.data
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export const deleteForm = createAsyncThunk('form/delete', async (formId, thunkApi) => {
  try{
    await axios.delete(config.serverUrl+'forms/'+formId)
    thunkApi.dispatch(setAlertWithTimeout('Form deleted successfully...','success'))
    return formId
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export default formSlice.reducer