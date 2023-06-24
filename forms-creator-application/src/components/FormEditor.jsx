import React from 'react'
import { Formik, Form, Field } from 'formik'
import { useDispatch } from 'react-redux'
import { updateForm } from '../slices/formSlice'
import { Link } from 'react-router-dom'


const FormEditor = ({form}) => {
  const dispatch = useDispatch()

  const onSubmit = (values) => {
    dispatch(updateForm(values))
  }

  const initialValues = {
    id: form.id,
    title:  form.title,
    enabled: form.enabled,
    description: form.description
  }

  return (
    <Formik
      initialValues={initialValues}
      onSubmit={onSubmit}
    >
      <Form className="row g-3 m-3 pb-2 border border-dark rounded">
        <div className='col-md-8'>
          <div className='m-2'>
            <Field className="form-control" id="inputTitle" name="title" placeholder="Title" />
          </div>
          <div className="m-2">
            <Field className="form-control" id="inputDescription" name="description" placeholder="Description" />
          </div>
        </div>
        <div className='col-md-4'>
          <div className='row m-2'>
            <div className="col-6 col-md-12 col-lg-6 form-check form-switch mb-2">
              <label className="form-check-label" htmlFor="inputEnabled">Enable Submissions</label>
              <Field className="form-check-input" type="checkbox" role="switch" id="inputEnabled" name="enabled" />
            </div>
            <div className='col-4 col-md-8 col-lg-6 text-end'>
              <Link to={`/form/${form.id}/view`}>Share Form</Link>
            </div>
            <div className="col-2 col-md-4 col-lg-12 text-end">
              <button type="submit" className='btn btn-outline-success'>Save</button>
            </div>
          </div>
        </div>
      </Form>
    </Formik>
  )
}

export default FormEditor