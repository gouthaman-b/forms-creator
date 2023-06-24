import React, { useEffect } from 'react'
import FormEditor from '../components/FormEditor'
import { useParams, Link } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import Loading from '../components/Loading'
import { getOneForm } from '../slices/formSlice'
import { createQuestion, getAllQuestions } from '../slices/questionSlice'
import QuestionEditor from '../components/QuestionEditor'

const EditForm = () => {
  const { id } = useParams()
  const form = useSelector((state) => state.form.form)
  const formLoading = useSelector((state) => state.form.formLoading)
  const questions = useSelector((state) => state.question.questions)
  const questionLoading = useSelector((state) => state.question.questionLoading)
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(getOneForm(id))
    dispatch(getAllQuestions(id))
  },[dispatch, id])
  
  const handleCreateQuestion = () => {
    console.log(id)
    dispatch(createQuestion(id))
  }

  return (
    <div>
      <div className='text-center d-flex justify-content-evenly'><Link to={`/form/${id}/edit`} className='fs-4 text-decoration-none'>Questions</Link> <Link to={`/form/${id}/res`} className='fs-4 text-decoration-none'>Responses</Link></div>
      
      {!formLoading && form!==null ? <FormEditor form={form}/> : <Loading/> }
      <hr/>
      {!questionLoading && questions!==null ? 
      (<div>
        {questions.map((question)=>(<QuestionEditor key={question.id} id={id} question={question} />))}
        <div className='text-center mt-2 mb-5'>
        <button onClick={handleCreateQuestion} className='btn btn-outline-dark'>Create Question</button>
        </div>
      </div>
      
      )
      :
      <Loading/> 
      }
    </div>
  )
}

export default EditForm