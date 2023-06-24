import { BrowserRouter, Route, Routes } from "react-router-dom"
import LoginForm from "./pages/LoginForm"
import RegisterForm from "./pages/RegisterForm"
import NavBar from "./components/NavBar"
import Alert from "./components/Alert"
import DashBoard from "./pages/DashBoard"
import PrivateRoute from './route'
import PageNotFound from "./pages/PageNotFound"
import { useDispatch } from "react-redux"
import { loadUser } from "./slices/authSlice"
import EditForm from './pages/EditForm'
import ViewForm from './pages/ViewForm'
import ViewResponses from './pages/ViewResponses'

function App() {
  
  const dispatch = useDispatch()
  dispatch(loadUser())

  return (
    <BrowserRouter>
      <div className="App">
        <NavBar/>
        <section className="container mt-3">
          <Alert/>
          <Routes>
          <Route exact path='/' element={ <PrivateRoute Component={DashBoard}/> } />
          <Route exact path='/form/:id/edit' element={ <PrivateRoute Component={EditForm}/> } />
          <Route exact path='/form/:id/res' element={ <PrivateRoute Component={ViewResponses}/> } />
          <Route exact path='/form/:id/view' element={ <PrivateRoute Component={ViewForm}/> } />
          <Route exact path='register' element={ <RegisterForm/> } />
          <Route exact path='login' element={ <LoginForm/> } />
          <Route exact path='*' element={ <PageNotFound/> } />
          </Routes>
        </section>
      </div>
    </BrowserRouter>
    
  )
}

export default App
