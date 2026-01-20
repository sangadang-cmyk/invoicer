import { createFileRoute, Outlet } from '@tanstack/react-router'

export const Route = createFileRoute('/user')({
  component: UserLayout,
})

function UserLayout() {
  return (
      <div className="p-2">
        <h1>User</h1>
        <Outlet />
      </div>
  )
}
