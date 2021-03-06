import IconButton from '@material-ui/core/IconButton';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import DeleteIcon from '@material-ui/icons/Delete';
import moment from 'moment';
import PropTypes from 'prop-types';
import React from 'react';

const TokensTable = ({ mobile, onDelete, tokens }) => (
  <Table size={mobile ? 'small' : 'medium'}>
    <TableHead>
      <TableRow>
        <TableCell>Issued</TableCell>
        <TableCell>Device</TableCell>
        <TableCell>Actions</TableCell>
      </TableRow>
    </TableHead>
    <TableBody>
      {tokens.map((token) => (
        <TableRow key={token.jwtId}>
          <TableCell component='th' scope='row'>
            {moment(token.issued).fromNow()}
          </TableCell>
          <TableCell>{token.source}</TableCell>
          <TableCell>
            <IconButton aria-label='Delete access token' onClick={() => onDelete(token)}>
              <DeleteIcon />
            </IconButton>
          </TableCell>
        </TableRow>
      ))}
    </TableBody>
  </Table>
);

TokensTable.propTypes = {
  mobile: PropTypes.bool,
  onDelete: PropTypes.func.isRequired,
  tokens: PropTypes.arrayOf(
    PropTypes.shape({
      issued: PropTypes.string.isRequired,
      jwtId: PropTypes.string.isRequired,
      source: PropTypes.string.isRequired,
    }),
  ).isRequired,
};

TokensTable.defaultProps = {
  mobile: false,
};

export default TokensTable;
