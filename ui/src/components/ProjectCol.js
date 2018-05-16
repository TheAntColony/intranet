import React, {Component} from 'react'
import {Link} from 'react-router-dom'

import {Row, Col, Tag} from 'antd'

class ProjectCol extends Component {
  getSkillTagsView(skills) {
    return skills.map(skill => {
      return (<Tag key={skill.id} color="blue">{skill.name}</Tag>)
    })
  }

  render() {
    var { project } = this.props
    return (
      <Col>
        <Row className="card-box">
          <Col>
            <h3>{project.name}</h3>
          </Col>
          <Col>
            <img className='projectImg' 
              src='https://newsignature.com/wp-content/uploads/2017/02/project-management-1024x512.png' alt='Project' />
          </Col>

          <Col className='marginTB20'>
            {this.getSkillTagsView(project.skills)}
          </Col>
          <Col> 
            <p>{project.description}</p> 
          </Col>
          <Col>
            <span className='fieldDesc'>Url: </span>
            <Link to={project.url}>{project.url}</Link>
          </Col>
          <Col span={12}>
            <span className='fieldDesc'>From: </span>
            <span>{this.props.getDateFormat(project.startDate)}</span>
          </Col>
          <Col span={12}>
            <span className='fieldDesc'>To: </span>
            <span>{this.props.getDateFormat(project.endDate)}</span>
          </Col>
        </Row>
      </Col>)
  }
}


export default ProjectCol
