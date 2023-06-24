import React from 'react'
import {Field} from 'formik'

const Dropdown = (props) => {
  const {name, options, ...rest} = props
  return (
    <Field as='select' id={name} name={name} className='form-select' {...rest}>
        {options.map(option => {
            return (
                <option key={option.id} value={option.text}>
                    {option.text}
                </option>
            )
        })}
    </Field>
  )
}

export default Dropdown