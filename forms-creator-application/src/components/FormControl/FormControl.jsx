import React from 'react'
import TextInput from './TextInput'
import Dropdown from './Dropdown'
import RadioGroup from './RadioGroup'
import CheckboxGroup from './CheckboxGroup'
import DatePicker from './DatePicker'

const FormControl = (props) => {
    const { control, ...rest } = props
    switch (control) {
      case 'url':
      case 'email':
      case 'text':
      case 'password':
      case 'tel':
      case 'number':
      case 'color':
      case 'time':
      case 'range':
        return <TextInput {...rest} />
      case 'select':
        return <Dropdown {...rest} />
      case 'radio':
        return <RadioGroup {...rest} />
      case 'checkbox':
        return <CheckboxGroup {...rest} />
      case 'date':
        return <DatePicker {...rest} />
      default:
        return null
    }
}

export default FormControl