import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Formik, Form } from 'formik'
import { getOneForm } from '../slices/formSlice'
import { useParams } from 'react-router-dom'
import { getAllQuestions } from '../slices/questionSlice'
import FormControl from '../components/FormControl/FormControl'
import { createResponse } from '../slices/responseSlice'
import { setAlertWithTimeout } from '../slices/alertSlice'

const ViewForm = () => {
  const { id } = useParams()
  const dispatch = useDispatch()
  const form = useSelector((state) => state.form.form)
  const formLoading = useSelector((state) => state.form.formLoading)
  const questions = useSelector((state) => state.question.questions)
  const questionLoading = useSelector((state) => state.question.questionLoading)

  useEffect(() => {
    dispatch(getOneForm(id))
    dispatch(getAllQuestions(id))
  },[dispatch, id])

  const onSubmit = (values) => {
    let responses = []
    for(let key in values){
      if(!Array.isArray(values[key])){
        responses.push({"questionId":key
        , "answer":values[key]})
      }
      else{
        responses.push({"questionId":key,"answer":values[key].join('!>')})
      }
      
    }
    dispatch(createResponse({formId:id,responses:responses}))
    dispatch(setAlertWithTimeout("Submitting the form...","info"))
    window.scrollTo(0,0)
  }

  const prepareQuestions = () => {
    let values = {}
    questions.forEach(question => {
     values[question.id] = ''
     if(question.type === 'number') values[question.id] = 0
     if(question.type === 'select') values[question.id] = question.options[0].text
     if(question.type === 'checkbox') values[question.id] = []
    })
    return values
  }

  return (
    <div>
      {!formLoading && form!==null && (
        <div className='border border-dark rounded p-2'>
          <h3>{form.title}</h3>
          <p>{form.description}</p>
        </div>
      )}
      {!questionLoading && questions!==null && questions!==[] && (
        <div className='mt-3'>
          <Formik initialValues={prepareQuestions()} onSubmit={onSubmit}>
            <Form>
              {questions.map(question => {
                if(question.type === 'number'){
                  return (
                    <div key={question.id} className='mt-2 border border-secondary rounded p-2'>
                      <label htmlFor={question.id}>{question.title} {question.required && <span className='text-danger'>*</span>}</label>
                      <FormControl control={question.type} type={question.type} name={question.id} min={question.min || undefined} max={question.max || undefined}/>
                    </div>
                  )
                }
                if(question.type === 'select' || question.type === 'checkbox' || question.type === 'radio'){
                  return (
                    <div key={question.id} className='mt-2 border border-secondary rounded p-2'>
                      <label htmlFor={question.id}>{question.title} {question.required && <span className='text-danger'>*</span>}</label>
                      <FormControl control={question.type} name={question.id} options={question.options} />
                    </div>
                  )
                }
                return (
                  <div key={question.id} className='mt-2 border border-secondary rounded p-2'>
                    <label htmlFor={question.id}>{question.title} {question.required && <span className='text-danger'>*</span>}</label>
                    <FormControl control={question.type} name={question.id} type={question.type} className='form-control' />
                  </div>
                )

              })}
              <div className='m-3 text-center'>
                <button type='submit' className='btn btn-dark'>submit</button>
              </div>
              
            </Form>
          </Formik>
        </div>
      )}
    </div>
  )
}

export default ViewForm