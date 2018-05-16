import React from 'react'
import { Link} from 'react-router-dom'
import { Button } from 'antd';
import config from './typeConfig';
import './Exception.css';

const Exception = ({ className, linkElement = 'a', type, title, desc, img, actions, ...rest }) => {
  const pageType = type in config ? type : '404';

  return (
    <div className={'exception'} {...rest}>
      <div className={'imgBlock'}>
        <div
          className={'imgEle'}
          style={{ backgroundImage: `url(${img || config[pageType].img})` }}
        />
      </div>
      <div className={'content'}>
        <h1>{title || config[pageType].title}</h1>
        <div className={'desc'}>{desc || config[pageType].desc}</div>
        <div>
          <Button type="primary"><Link to='/'>Back</Link></Button>
        </div>
      </div>
    </div>
  );
};

export default Exception;
