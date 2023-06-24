import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Loading from '../components/Loading'
import { createForm, getAllForms } from '../slices/formSlice'
import FormCard from '../components/FormCard'


const DashBoard = () => {
  const dispatch = useDispatch()
  const forms = useSelector((state)=>state.form.forms)
  const isLoading = useSelector((state)=>state.form.formLoading)

  const handleClickCreateForm = (e) => {
    dispatch(createForm())
  }

  useEffect(()=>{
    dispatch(getAllForms())
  }, [dispatch])


  return (
    <div>
      <h3>Recent Forms</h3>
      {isLoading ?
        <Loading/>:
        <>
        <button onClick={handleClickCreateForm} disabled={isLoading} className='btn btn-outline-dark'>Create Form</button>

        {forms!=null && forms.length>0 ? (
            <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4 mt-3">
            {forms.map(form => (
              <FormCard key={form.id} form={form}/>
            ))}
            </div>
          )
          :
          <div className="mt-4 text-center fs-5">No Forms</div>
        }
        </>

      }
    </div>
  )
}

export default DashBoard