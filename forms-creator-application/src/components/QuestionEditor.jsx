import React from 'react'
import FormControl from './FormControl/FormControl'
import { Formik, Form, Field, FieldArray } from 'formik'
import { useDispatch, useSelector } from 'react-redux'
import { deleteQuestion, updateQuestion } from '../slices/questionSlice'
import { setAlertWithTimeout } from '../slices/alertSlice'

const QuestionEditor = ({id, question}) => {
  const questionLength = useSelector((state) => state.question.questionLength)
  const dispatch = useDispatch()

  const onSubmit = (values) => {
    let question = values
    if(question.type!=='number'){
      question.min=null
      question.max=null
    }
    if(question.type==='number'){
      if(question.min===0) question.min = null
      if(question.max===0) question.max = null
    }
    if(question.type!=='checkbox' && question.type!=='select' && question.type!=='radio'){question.options=null}
    if(question.type==='checkbox' || question.type==='select' ||  question.type==='radio'){
      question.options = values.options.map((option) => option.text)
    }
    dispatch(updateQuestion({formId:id,question:values}))
  }

  const initialValues = {
    id: question.id,
    title: question.title,
    description: question.description,
    type: question.type,
    required: question.required,
    min: question.min || 0,
    max: question.max || 0,
    options: (question.options && question.options.length>0)? question.options : [{id:'',text:'Enter option'}],
    orderNumber: question.orderNumber
  }

  const selectOptions = [
    {id:1, text:'text'},
    {id:2, text:'email'},
    {id:3, text:'select'},
    {id:4, text:'checkbox'},
    {id:5, text:'radio'},
    {id:6, text:'number'},
  ]

  const deleteHandler = () => {
    if(questionLength>1){
      dispatch(deleteQuestion({formId:id, question:question}))
    }else{
      dispatch(setAlertWithTimeout("Cannot delete question", "danger"))
    }
  }

  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit}>
      {({values}) => (
        
        <Form className='border border-secondary rounded p-2 m-3'>
          {console.log(values)}
          <div className='row'>
            <div className='col-8'>
              <FormControl control='text' name='title'/>
            </div>
            <div className='col-4'>
              <FormControl control='select' name='type' options={selectOptions} />
            </div>
            
          </div>
          
          {(values.type==='number') && (
            <div>
              <hr />
              <div className="input-group">
                <span className="input-group-text">Min & Max</span>
                <FormControl control='number' name='min' type='number' />
                <FormControl control='number' name='max' type='number'/>
              </div>
            </div>
          )}
          {(values.type==='checkbox' || values.type==='select' || values.type==='radio') && (
            <div>
              <hr />
              <FieldArray name="options">
              {({ push, remove }) => (
                <div className='m-1'>
                  <button type="button" onClick={() => push({id:'',text:'Enter option'})} className='btn btn-outline-dark'>
                    Add
                  </button>
                  {values.options.map((option, index) => (
                    <div key={index} className='row mt-1'>
                      <div className='col-9 col-md-10'>
                        <Field name={`options.${index}.text`} className='form-control '/>
                      </div>
                      
                      {index>0 && (
                        <div className='col-3 col-md-2'>
                          <button type="button" onClick={() => remove(index)} className='btn btn-outline-danger'>
                            Remove
                          </button>
                        </div>
                      )}
                      
                    </div>
                  ))}
                </div>
              )}
              </FieldArray>
            </div>
          )}
          <hr />
          <div className='row m-2'>
            <div className="col-8 form-check form-switch">
              <label className="form-check-label" htmlFor="req">Required</label>
              <Field className="form-check-input" type="checkbox" role="switch" id="req" name="required" />
            </div>
            <div className='col-4 d-flex justify-content-end'>
              <div className='m-1'>
                <button type="submit" className='btn btn-outline-success'>Save</button>
              </div>
              <div className='m-1'>
                <button type="button" onClick={deleteHandler} className='btn btn-outline-danger'>Delete</button>
              </div> 
            </div>
          </div>
        </Form>
      )}
        
    </Formik>
  )
}

export default QuestionEditor