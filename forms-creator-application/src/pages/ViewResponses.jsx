import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { getAllResponses } from '../slices/responseSlice'
import { useParams } from 'react-router-dom'
import { getAllQuestions } from '../slices/questionSlice'
import { Link } from 'react-router-dom'
import { CSVLink } from 'react-csv'
import ResponseTable from '../components/ResponseTable'

const prepareData = (questions, responses) => {
  let data = []
  for(const response of responses) {
    let keyStore = {}
    keyStore["resId"] = response.id
    keyStore["email"] = response.userEmail
    keyStore["time"] = response.createdAt 
    keyStore["response"] = {}
    for(const res of response.responses){
      const question = questions.find(q => q.id===res.questionId)
      if(question!==undefined && question.title!==undefined){
        if(question.type==='checkbox') keyStore["response"][question.title] = res.answer.split('!>').join()
        else keyStore["response"][question.title] = res.answer 
      }
    }
    data.push(keyStore)
  }
  console.log(data)
  return data
}

const prepareHeaders = (questions) => {
  let headers = []
  headers.push({label:'resId',key:'resId'})
  headers.push({label:"email",key:'email'})
  headers.push({label:"time",key:'time'})
  for (const q of questions){
    headers.push({label:q.title, key:'response.'+q.title})
  }
  console.log(headers)
  return headers
  
}

const ViewResponses = () => {
  const {id} = useParams()
  const dispatch = useDispatch()
  const responseLength = useSelector((state) => state.response.responseLength)
  const responses = useSelector((state) => state.response.responses)

  const questions = useSelector((state) => state.question.questions)

  const [preparedData, setPreparedData] = useState([])
  const [csvHeaders, setCsvHeaders] = useState([])

  useEffect(() => {
    dispatch(getAllResponses(id))
    dispatch(getAllQuestions(id))
  }, [dispatch, id])

  useEffect(() => {
    if(questions.length>0 && responses.length>0){
      setPreparedData(prepareData(questions, responses))
      setCsvHeaders(prepareHeaders(questions))
    }
  },[questions, responses])

  return (
    <div>
      <div className='text-center d-flex justify-content-evenly'><Link to={`/form/${id}/edit`} className='fs-4 text-decoration-none'>Questions</Link> <Link to={`/form/${id}/res`} className='fs-4 text-decoration-none'>Responses</Link></div>
      {preparedData!==[] && (
        <div className='mt-4'>
          {console.log(JSON.stringify(preparedData))}
          <div className='mx-1 row border border-dark p-4 rounded'>
            <div className='col-6 fs-4'>
              {`${responseLength>0? responseLength:"No"} response${responseLength>1? "s":""}`}
            </div>
            <div className='col-6 text-end mr-3'>
              {responseLength>0 ? (
                <CSVLink data={preparedData} headers={csvHeaders} className='btn btn-outline-success'>Export CSV</CSVLink>
              ) : (
                <span className='text-muted'>Export</span>
              )}
            </div>
          </div>
          {preparedData.length>0 && (
            <div>
              <ResponseTable tableData={preparedData} headers={csvHeaders} />
            </div>
          )}
        </div>
      ) }
    </div>
  )
}

export default ViewResponses