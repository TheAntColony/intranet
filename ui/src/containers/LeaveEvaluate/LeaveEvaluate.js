import React, {Component} from 'react'
import { Row, message } from 'antd'

import Leave from '../../services/Leave'

import LeaveEvaluateCol from '../../components/LeaveEvaluateCol/LeaveEvaluateCol'

import './LeaveEvaluate.css'

class LeaveEvaluate extends Component {

  constructor(props) {
    super(props)

    this.removeLeaveFromList = this.removeLeaveFromList.bind(this)
    this.handleEvaluationComment = this.handleEvaluationComment.bind(this)
    this.approveLeave = this.approveLeave.bind(this)
    this.rejectLeave = this.rejectLeave.bind(this)
  }

  state = {
    size: 'default',
    pendingLeaves: [],
    recievedLeaves: false
  }

  componentDidMount() {
    this.getPendingLeaves()
  }

  /**
   * API based methods
   */
  getPendingLeaves() {
    Leave.getPending()
      .then((response) => {
        this.setState({ 
          pendingLeaves: response.data,
          recievedLeaves: true
        })
      })
  }

  updateLeave(leave) {
    Leave.update(leave)
      .then((response) => {
        console.log(response)
        this.removeLeaveFromList(leave.id)
      }).catch((err) => message.error(err.response))
  }

  /**
   * Helper method to remove leave when it is approved or rejected from list
   * @param {*} id 
   */
  removeLeaveFromList(id) {
    var pendingLeaves = this.state.pendingLeaves.filter(leave => leave.id !== id)
    this.setState({ pendingLeaves})
  }

  /**
   * Event based methods to handle input change and button clicks
   */
  handleEvaluationComment(event, index) {
    var pendingLeaves = this.state.pendingLeaves
    pendingLeaves[index].evaluationComment = event.target.value
    this.setState({ pendingLeaves })
  }

  approveLeave(leave) {
    leave.status = Leave.status.Approved
    this.updateLeave(leave)
    console.log('Approved', leave)
  }

  rejectLeave(leave) {
    leave.status = Leave.status.Rejected
    this.updateLeave(leave)
    console.log('Rejected', leave)
  }
  
  render() {
    var leaveColViews = this.state.pendingLeaves.map((leave, index) => {
      return (
        <LeaveEvaluateCol key={leave.id}
          approveLeave={this.approveLeave}
          rejectLeave={this.rejectLeave}
          leave={leave}
          index={index}
          span={12}
          pendingLeaves={this.state.pendingLeaves}
          handleEvaluationComment={this.handleEvaluationComment} />)
    })

    if(this.state.recievedLeaves && 
      this.state.pendingLeaves.length === 0) {
        leaveColViews = (
        <div className='flexCenter'>
          <p className='leavesEvaluatedP'>All leaves evaluated</p>
        </div>)
      }


    return (
      <div>
        <h2>Requests to evaluate</h2>
        <Row>
          {leaveColViews}
        </Row>
      </div>
    );
  }
}

export default LeaveEvaluate 
