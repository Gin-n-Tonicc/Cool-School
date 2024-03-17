import InternalError from '../internal-error/InternalError';

import { Component, ErrorInfo, ReactNode } from 'react';

interface Props {
  children?: ReactNode;
}

interface State {
  hasError: boolean;
}

// The component which prevents the app from completely crashing, when
// an error in the code occurs, by rendering a specified component.
// Has to be a class component
class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
  };

  public static getDerivedStateFromError(_: Error): State {
    return { hasError: true };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught error: ', error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return <InternalError />;
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
