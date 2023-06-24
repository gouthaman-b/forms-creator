import React from 'react'
import {Field} from 'formik'

const RadioGroup = (props) => {
    const {name, options, ...rest} = props
  return (
    <Field name={name}>
        {({ field }) => {
          return options.map(option => {
            return (
              <div key={option.id} className='form-check'>
                <input
                  type='radio'
                  id={option.id}
                  {...field}
                  {...rest}
                  value={option.text}
                  checked={field.value === option.text}
                  className='form-check-input'
                />
                <label htmlFor={option.id} className='form-check-label'>{option.text}</label>
              </div>
            )
          })
        }}
    </Field>
  )
}

export default RadioGroup