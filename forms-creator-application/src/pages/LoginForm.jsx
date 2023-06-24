import React from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { Navigate, Link } from 'react-router-dom';
import { login } from '../slices/authSlice'
import { Formik, Form } from 'formik';
import FormControl from '../components/FormControl/FormControl';

const LoginForm = () => {
  const dispatch = useDispatch()
  const token = useSelector((state) => state.auth.token)
  const loading = useSelector((state) => state.auth.authLoading)

  const initialValues = {
    email: '',
    password: ''
  }

  const onSubmit = (values) => {
    dispatch(login({email:values.email, password:values.password}))
  }

  if(token && !loading){
    return <Navigate to='/' />
  }

  return (
    <div>
    <h3 className='text-center mt-5'>Login</h3>
    <div className='container'>
      <div className='row justify-content-center align-items-center'>
        <div className='col-md-6'>
          <div className='col-md-12'>
            <Formik initialValues={initialValues} onSubmit={onSubmit}>
            <Form className='form mt-4'>
              <div className='form-group m-2'>
                <FormControl control='email' type='email' name='email' placeholder='Email' />
              </div>
              <div className='form-group m-2'>
                <FormControl control='password' type='password' name='password' placeholder='Password'  />
              </div>
              <div className='text-center mt-3'>
                <button type='submit' disabled={loading} className='btn btn-outline-dark'>
                  {loading ? 'Processing...' : 'Login'}
                </button>
              </div>
            </Form>
            </Formik>
            <div className='text-center mt-2'>
              <Link to='/register' className='text-decoration-none'>
                Register
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
  )
}

export default LoginForm
