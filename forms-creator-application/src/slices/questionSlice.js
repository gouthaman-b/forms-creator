import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import axios from 'axios'
import config  from '../config'
import { setAlertWithTimeout } from './alertSlice'

const initialState = {
    questionLoading: false,
    questions: [],
    questionLength: 0,
    questionError: null
}

const questionSlice = createSlice({
    name: 'question',
    initialState,
    reducers: {},
    extraReducers: builder => {
        builder
        .addCase(getAllQuestions.fulfilled, (state, action) => {
            state.questions = action.payload
            state.questionLength = state.questions.length
            state.questionLoading = false
        })
        .addCase(getAllQuestions.pending, (state) => {
          state.questionLoading = true
          state.questionError = null
        })
        .addCase(getAllQuestions.rejected, (state, action) => {
          state.questionLoading = false
          state.questionError = action.payload
        })

        .addCase(createQuestion.fulfilled, (state, action) => {
          state.questionLoading = false
          state.questions.push(action.payload)
          state.questionLength = state.questions.length
        })
        .addCase(createQuestion.pending, (state) => {
          state.questionLoading = true
          state.questionError = null
        })
        .addCase(createQuestion.rejected, (state, action) => {
          state.questionLoading = false
          state.questionError = action.payload
        })

        .addCase(updateQuestion.fulfilled, (state, action) => {
          state.questionLoading = false
          state.questions = state.questions.map(question => question.id!==action.payload.id ? question : action.payload)
          state.questionLength = state.questions.length
        })
        .addCase(updateQuestion.pending, (state) => {
          state.questionLoading = true
          state.questionError = null
        })
        .addCase(updateQuestion.rejected, (state, action) => {
          state.questionLoading = false
          state.questionError = action.payload
        })

        .addCase(deleteQuestion.fulfilled, (state, action) => {
          state.questionLoading = false
          state.questions = state.questions.filter(question => question.id !== action.payload)
          state.questionLength = state.questions.length
        })
        .addCase(deleteQuestion.pending, (state) => {
          state.questionLoading = true
          state.questionError = null
        })
        .addCase(deleteQuestion.rejected, (state, action) => {
          state.questionLoading = false
          state.questionError = action.payload
        })
    }
})

export const getAllQuestions = createAsyncThunk('question/getAll', async (formId, thunkApi) => {
  try{
    const response = await axios.get(config.serverUrl+'forms/'+formId+'/questions/')
    return response.data
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export const createQuestion = createAsyncThunk('question/create', async (formId, thunkApi) => {
  try{
    const question = {
      'title': 'Untitled Question',
      'description': 'Untitled description',
      'type': 'text',
      'required':false,
    }
    const response = await axios.post(config.serverUrl+'forms/'+formId+'/questions/',  question)
    return response.data
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export const updateQuestion = createAsyncThunk('question/update', async ({formId, question}, thunkApi) => {
  try{
    const response = await axios.put(config.serverUrl+'forms/'+formId+'/questions/'+question.id,  question)
    thunkApi.dispatch(setAlertWithTimeout('Question updated successfully','success'))
    return response.data
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})

export const deleteQuestion = createAsyncThunk('question/delete', async ({formId, question}, thunkApi) => {
  try{
    await axios.delete(config.serverUrl+'forms/'+formId+'/questions/'+question.id)
    thunkApi.dispatch(setAlertWithTimeout('Question deleted successfully','success'))
    return question.id
  }catch(error){
    thunkApi.dispatch(setAlertWithTimeout(error.response.data.message,'danger'))
    return thunkApi.rejectWithValue(error.response.data)
  }
})


export default questionSlice.reducer