import React from 'react'
import { Link } from 'react-router-dom'
import formatDistanceToNow from 'date-fns/formatDistanceToNow'
import { parse } from 'date-fns'
import { FaTrash } from 'react-icons/fa'
import { useDispatch } from 'react-redux'
import { deleteForm } from '../slices/formSlice'


const FormCard = (props) => {
    const form = props.form
    const dispatch = useDispatch()

    const handleDelete = () => {
      dispatch(deleteForm(form.id))
    }

  return (
    <div className='col'>
      <div className="card h-100 border border-dark" id={form.id}>
        <div className="card-body">
          <h5 className="card-title"><Link to={`/form/${form.id}/edit`} className='text-decoration-none'>{form.title}</Link></h5>
          <p className="card-text">{form.description}</p>
        </div>
        <div className="card-footer">
          <div className='row'>
          <div className='col-9'>
            Created: {formatDistanceToNow(parse(form.createdAt,"dd-MM-yyyy hh:mm:ss a",new Date()) , { addSuffix: true })}
          </div>
          <div className='col-3'>
          <button className='btn btn-outline-danger' onClick={handleDelete}><FaTrash/></button>
          </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default FormCard