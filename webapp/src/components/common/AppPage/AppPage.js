import Container from '@material-ui/core/Container';
import { makeStyles } from '@material-ui/core/styles';
import PropTypes from 'prop-types';
import React, { useState } from 'react';

import NavBar from '../NavBar';
import NavDrawer from '../NavDrawer';

const useStyles = makeStyles((theme) => ({
  appBarSpacer: theme.mixins.toolbar,
  container: {
    paddingBottom: theme.spacing(4),
    paddingTop: theme.spacing(4),
  },
  content: {
    flexGrow: 1,
    height: '100vh',
    overflow: 'auto',
  },
}));

const AppPage = ({ children, title }) => {
  const [isDrawerOpen, setDrawerOpen] = useState(false);

  const handleToggleDrawer = () => setDrawerOpen((old) => !old);
  const handleCloseDrawer = () => setDrawerOpen(false);

  const classes = useStyles();

  return (
    <div style={{ display: 'flex' }}>
      <NavBar onToggleDrawer={handleToggleDrawer} title={title} />
      <NavDrawer onClose={handleCloseDrawer} open={isDrawerOpen} />
      <main className={classes.content}>
        <div className={classes.appBarSpacer} />
        <Container className={classes.container} maxWidth='lg'>
          {children}
        </Container>
      </main>
    </div>
  );
};

AppPage.propTypes = {
  children: PropTypes.oneOfType([PropTypes.element, PropTypes.arrayOf(PropTypes.element)])
    .isRequired,
  title: PropTypes.string,
};

AppPage.defaultProps = {
  title: 'UnderBudget',
};

export default AppPage;
