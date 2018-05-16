import React, {Component} from 'react';

import {
  Redirect
} from 'react-router-dom';
import User from '../../services/User'
import UserAuth from '../../services/UserAuth'
import SignupForm from '../../components/SignupForm'

import './Signup.css'

class Signup extends Component {
  state = {
    redirectToReferrer: false,
    error: null
  };

  signup = (email, username, password) => {
    var data = {
      firstName: '',
      lastName: '',
      email: email,
      username: username,
      password: password
    }

    User.post(data)
      .then((response) => {
        UserAuth.authenticate(data.email, data.password, (err, response) => {
          if(err) {
            this.setState({ error: err })
          } else {
            this.setState({ redirectToReferrer: true })
          }
        })
      }).catch((e) => {
        this.setState({ error: e })
      })
  }

  handleSubmit = (e, form) => {
    e.preventDefault();
    form.validateFields((err, values) => {
      if (!err) {
        this.signup(values.email, values.userName, values.password)
      }
    })
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
        <SignupForm error={this.state.error} 
          handleSubmit={this.handleSubmit.bind(this) } 
          onErrorClose={this.onErrorClose.bind(this)} />
      </div>
    );
  }
}

export default Signup
