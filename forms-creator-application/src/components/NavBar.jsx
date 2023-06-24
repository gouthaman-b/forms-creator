import React from 'react'
import { useSelector, useDispatch } from 'react-redux';
import { logout } from '../slices/authSlice';
import { Link } from 'react-router-dom';

const NavBar = () => {
    const token = useSelector((state) => state.auth.token)
    const email = useSelector((state) => state.auth.email)
    const loading = useSelector((state) => state.auth.authLoading)
    const dispatch = useDispatch()

    const handleLogout = () => {
      dispatch(logout())
    }

  return (
    <nav className="navbar sticky-top navbar-expand-lg bg-white border-bottom border-dark mx-3">
      <span className="navbar-brand"><Link to="/" className='text-decoration-none text-dark fw-bold'>Spring Forms</Link></span>

      {!loading && token!=null ? 
      <>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
            <div className="d-flex justify-content-center align-items-center rounded-circle border border-dark font-weight-bold mx-3" style={{width:'40px', height:'40px'}}> 
              <p className="m-0 p-0">{email[0].toUpperCase()}</p> 
            </div>
            <button className="btn btn-outline-dark" onClick={handleLogout}>Logout</button>
        </div>
      </>:null
      }
    </nav>
  )
}

export default NavBar