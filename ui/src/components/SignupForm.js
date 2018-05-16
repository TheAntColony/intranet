import React, {Component} from 'react'
import { Form, Icon, Input, Button, Alert } from 'antd';

const FormItem = Form.Item;

class NormalSignupForm extends Component {
  render() {
    const { getFieldDecorator } = this.props.form;
    var errorAlert = <Alert
      message="Error"
        description='Invalid email or password'
        type="error"
        closable
        onClose={this.onErrorClose}
      />

    var signupForm = 
      <Form onSubmit={(event) => this.props.handleSubmit(event, this.props.form)} className="login-form">
        <FormItem>
          {getFieldDecorator('email', {
            rules: [{ required: true, message: 'Please input your email!' }],
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Email" />
          )}
        </FormItem>
        <FormItem>
          {getFieldDecorator('userName', {
            rules: [{ required: true, message: 'Please input your username!' }],
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Username" />
          )}
        </FormItem>
        <FormItem>
          {getFieldDecorator('password', {
            rules: [{ required: true, message: 'Please input your Password!' }],
          })(
            <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" placeholder="Password" />
          )}
        </FormItem>
        <FormItem>
          <p className="login-form-forgot"></p>
          <Button type="primary" htmlType="submit" className="login-form-button">
            Sign up
          </Button>
          Or <a href="/login">login now!</a>
        </FormItem> 

        {this.props.error !== null ? errorAlert : null}
      </Form>

      return (signupForm)
  }

}

const SignupForm = Form.create()(NormalSignupForm);

export default SignupForm
