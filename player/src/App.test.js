import { render, screen } from '@testing-library/react';
import App from './App';

test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByText(/Enter address of your local commander/i);
  expect(linkElement).toBeInTheDocument();
});
