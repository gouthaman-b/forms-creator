import { configureStore } from "@reduxjs/toolkit"
import authReducer from './slices/authSlice'
import alertReducer from './slices/alertSlice'
import formReducer from './slices/formSlice'
import questionReducer from './slices/questionSlice'
import responseReducer from './slices/responseSlice'

export const store = configureStore({
    reducer:{
        auth: authReducer,
        alert: alertReducer,
        form: formReducer,
        question: questionReducer,
        response: responseReducer
    }
})