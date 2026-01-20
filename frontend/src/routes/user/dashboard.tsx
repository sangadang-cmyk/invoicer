import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/user/dashboard')({
  component: UserDashboard,
})

function UserDashboard() {
  return (
    <div className="p-2">
      <h2>User Dashboard</h2>
      <p>User dashboard placeholder</p>
    </div>
  )
}
