import React from 'react';
import { Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

const PrivateRoute = ({Component}) => {
    const token = useSelector((state) => state.auth.token)
    const loading = useSelector((state) => state.auth.loading)
    if (token && !loading ) return <Component/> 
    else{
        return <Navigate to="/login" />
    }
}

export default PrivateRoute