/* eslint-disable react/jsx-props-no-spreading */

import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import PropTypes from 'prop-types';
import React from 'react';

import actionPropsShape from '../../utils/action-props';

const ActionMenu = ({ actions, anchor, onClose }) => (
  <Menu
    anchorEl={anchor}
    anchorOrigin={{
      horizontal: 'right',
      vertical: 'top',
    }}
    onClose={onClose}
    open={Boolean(anchor)}
    transformOrigin={{
      horizontal: 'right',
      vertical: 'top',
    }}
  >
    {actions.map((action) => (
      <MenuItem key={action.text} {...action}>
        {action.text}
      </MenuItem>
    ))}
  </Menu>
);

ActionMenu.propTypes = {
  actions: PropTypes.arrayOf(actionPropsShape).isRequired,
  anchor: PropTypes.shape({}),
  onClose: PropTypes.func.isRequired,
};

ActionMenu.defaultProps = {
  anchor: null,
};

export default ActionMenu;
