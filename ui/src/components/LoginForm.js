import React, {Component} from 'react'
import { Form, Icon, Input, Button, Checkbox, Alert } from 'antd';
const FormItem = Form.Item;

class NormalLoginForm extends Component {
  render() {
    const { getFieldDecorator } = this.props.form;
    var errorAlert = <Alert
      message="Error"
        description='Invalid email or password'
        type="error"
        closable
        onClose={this.onErrorClose}
      />

    var loginForm = 
      <Form onSubmit={ (event) => this.props.handleSubmit(event, this.props.form) } className="login-form">
        <FormItem>
          {getFieldDecorator('userName', {
            rules: [{ required: true, message: 'Please input your email!' }],
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Email" />
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
          {getFieldDecorator('remember', {
            valuePropName: 'checked',
            initialValue: true,
          })(
            <Checkbox>Remember me</Checkbox>
          )}
          <a className="login-form-forgot" href="">Forgot password</a>
          <Button type="primary" htmlType="submit" className="login-form-button">
            Log in
          </Button>
          Or <a href="/signup">register now!</a>
        </FormItem> 

        {this.props.error !== null ? errorAlert : null}
      </Form>

      return (loginForm)
  }
}

const LoginForm = Form.create()(NormalLoginForm);

export default LoginForm
