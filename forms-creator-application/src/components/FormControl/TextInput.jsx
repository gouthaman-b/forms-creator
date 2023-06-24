import React from 'react'
import { Field } from 'formik'

const TextInput = (props) => {
  const { name, ...rest } = props
  return (
    <Field id={name} name={name} {...rest} className='form-control border border-dark' />
  )
}

export default TextInput