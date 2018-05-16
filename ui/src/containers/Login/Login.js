import React, {Component} from 'react';

import {
  Redirect
} from 'react-router-dom';
import UserAuth from '../../services/UserAuth'
import LoginForm from '../../components/LoginForm'

import './Login.css'
class Login extends Component {
  state = {
    redirectToReferrer: false,
    error: null
  };

  login = (email, password) => {
    UserAuth.authenticate(email, password, (err, response) => {
      if (err) {
        this.setState({ error: err })
      } else {
        this.setState({ redirectToReferrer: true });
      }
    });
  };

  handleSubmit = (e, form) => {
    e.preventDefault();
    form.validateFields((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
        this.login(values.userName, values.password)
      }
    });
  }

  onErrorClose = () => {

  }

  render() {
    const { from } = (this.props.location && this.props.location.state) || { from: { pathname: "/" } };
    const { redirectToReferrer } = this.state;

    if (redirectToReferrer) {
      return <Redirect to={from} />;
    }

    return (
      <div className="container">
        <LoginForm error={this.state.error} 
          handleSubmit={this.handleSubmit.bind(this) } onErrorClose={this.onErrorClose.bind(this)} />
      </div>
    );
  }
}

export default Login
