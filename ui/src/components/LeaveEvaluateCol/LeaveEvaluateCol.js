import React, {Component} from 'react'
import { Button, Row, Col, Input, Divider } from 'antd'

class LeaveEvaluateCol extends Component {
  render() {
    var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

    return (          
      <Col span={this.props.span} key={this.props.leave.id}>
        <div  className='card'>
          <Row className='row'>
            <Col span={4} className='colDesc'>Type:</Col>
            <Col span={8}>{this.props.leave.category.name}</Col>
            <Col span={4} className='colDesc'>User:</Col>
            <Col span={8}>{this.props.leave.user.email}</Col>
          </Row>
          <Row className='row'>
            <Col span={4} className='colDesc'>Reason: </Col>
            <Col span={20}>{this.props.leave.reason}</Col>
          </Row>
          <Row className='row'>
            <Col span={4} className='colDesc'>From: </Col>
            <Col span={20}>{new Date(this.props.leave.startDate).toLocaleDateString('en-US',options)} </Col>
          </Row>
          <Row className='row'>
            <Col span={4} className='colDesc'>To: </Col>
            <Col span={20}>{new Date(this.props.leave.endDate).toLocaleDateString('en-US',options)} </Col>
          </Row>
          <Divider />
          <Row> 
            <Col span={24} className='colDesc'>
              <Input className='reason' placeholder='Enter evaluation comment' 
                value={this.props.pendingLeaves[this.props.index].evaluationComment} 
                onChange={ (event) => this.props.handleEvaluationComment(event, this.props.index) } />
            </Col>
          </Row>
          <Row className='row flexCenter' gutter={16}>
            <Col>
              <Button type='default' onClick={() => this.props.approveLeave(this.props.leave)}>Approve</Button>
            </Col>
            <Col>
              <Button type='danger' onClick={() => this.props.rejectLeave(this.props.leave)}>Reject</Button>
            </Col>
          </Row> 
        </div>
      </Col>)
  }
}

export default LeaveEvaluateCol
